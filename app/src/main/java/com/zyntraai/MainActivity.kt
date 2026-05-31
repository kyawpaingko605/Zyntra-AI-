package com.zyntraai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

// ပုံထဲကအတိုင်း သန့်စင်တောက်ပသော ကာလာ Palette
val ChatGPTBlue = Color(0xFF0D6EFD)       // စာပို့ bubble အပြာရောင်
val ChatGPTLightBg = Color(0xFFFFFFFF)    // နောက်ခံအဖြူရောင်
val ChatGPTTextDark = Color(0xFF111111)   // စာသားအမည်းရောင်
val ChatGPTHintGray = Color(0xFF6E6E80)   // စာသားအမှုံရောင်
val ChatGPTBubbleAI = Color(0xFFF7F7F8)   // AI ရဲ့ မီးခိုးဖျော့ bubble ရောင်
val ChatGPTBorder = Color(0xFFE5E5E5)     // စည်းကြောင်းအရောင်

// ======= [1. BACKEND LOGIC: API NETWORKING] =======

data class OpenAIRequest(val model: String = "gpt-3.5-turbo", val messages: List<ApiMessage>)
data class ApiMessage(val role: String, val content: String)
data class OpenAIResponse(val choices: List<Choice>)
data class Choice(val message: ApiMessage)

interface OpenAiService {
    @POST("v1/chat/completions")
    suspend fun getChatCompletion(
        @Header("Authorization") apiKey: String,
        @Body request: OpenAIRequest
    ): OpenAIResponse
}

object RetrofitClient {
    val instance: OpenAiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://openai.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenAiService::class.java)
    }
}

// ======= [2. ARCHITECTURE: STATE MANAGEMENT] =======

data class ChatMessage(val id: String, val text: String, val isUser: Boolean)
data class HistoryChat(val title: String, val time: String)

class ChatViewModel : ViewModel() {
    var messages by mutableStateOf(listOf<ChatMessage>())
    var isLoading by mutableStateOf(false)

    // ပုံထဲက နမူနာသမိုင်းကြောင်း Chats စာရင်း
    val chatHistory = listOf(
        HistoryChat("Project Spark Ideas", "3 hours ago"),
        HistoryChat("Vacation Planning", "Yesterday"),
        HistoryChat("UI Design Feedback", "2 days ago"),
        HistoryChat("Recipe for Lasagna", "3 days ago")
    )

    private val apiKey = "Bearer ${BuildConfig.OPENAI_API_KEY}" 

    fun sendMessage(text: String, onComplete: () -> Unit) {
        val userMessage = ChatMessage(id = System.nanoTime().toString(), text = text, isUser = true)
        messages = messages + userMessage
        isLoading = true
        onComplete()

        MainScope().launch {
            try {
                val apiMessages = messages.map { 
                    ApiMessage(role = if (it.isUser) "user" else "assistant", content = it.text) 
                }
                val response = RetrofitClient.instance.getChatCompletion(apiKey, OpenAIRequest(messages = apiMessages))
                val aiResponseText = response.choices.firstOrNull()?.message?.content ?: "အဖြေမရှိပါ။"
                messages = messages + ChatMessage(id = System.nanoTime().toString(), text = aiResponseText, isUser = false)
            } catch (e: Exception) {
                messages = messages + ChatMessage(id = System.nanoTime().toString(), text = "ချိတ်ဆက်မှု အဆင်မပြေပါ", isUser = false)
            } finally {
                isLoading = false
                onComplete()
            }
        }
    }
}

// ======= [3. UI DESIGN: MAIN SCREEN LAYER] =======

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val myViewModel: ChatViewModel = viewModel()
                ChatGPTUiScreen(myViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatGPTUiScreen(viewModel: ChatViewModel) {
    var inputMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("ChatGPT", color = ChatGPTBlue, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    Icon(Icons.Default.Add, "New", tint = ChatGPTBlue, modifier = Modifier.padding(start = 12.dp).size(26.dp))
                },
                actions = {
                    Icon(Icons.Default.Search, "Search", tint = ChatGPTBlue, modifier = Modifier.padding(end = 12.dp).size(26.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ChatGPTLightBg)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(ChatGPTLightBg)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // ၁။ Chats ရာဇဝင်ခေါင်းစဉ်အပိုင်း
                item {
                    Text(
                        text = "Chats",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = ChatGPTTextDark,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }

                // ၂။ သမိုင်းကြောင်းဖိုင်စာရင်းများ (နမူနာပြအတိုင်း)
                items(viewModel.chatHistory) { history ->
                    HistoryItemRow(history)
                    HorizontalDivider(color = ChatGPTBorder, thickness = 0.5.dp)
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                // ၃။ တကယ့် API စကားပြောခန်း မက်ဆေ့ခ်ျများပြသခြင်း
                items(viewModel.messages, key = { it.id }) { message ->
                    ChatBubbleRow(message)
                }

                if (viewModel.isLoading) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(8.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = ChatGPTBlue, strokeWidth = 2.dp)
                        }
                    }
                }
            }

            // ၄။ အောက်ခြေ အဝိုင်းပုံစံ Input Box Layout
            BottomInputBar(
                text = inputMessage,
                onTextChange = { inputMessage = it },
                onSendClick = {
                    if (inputMessage.isNotBlank()) {
                        val currentText = inputMessage
                        inputMessage = ""
                        viewModel.sendMessage(currentText) {
                            // စာပို့ပြီးလျှင် auto scroll လုပ်ရန် ဤနေရာ၌ ထိန်းချုပ်နိုင်ပါသည်
                        }
                    }
                }
            )
        }
    }
}

// ======= [၄။ UI COMPONENT တစ်ခုချင်းစီစာရင်း] =======

@Composable
fun HistoryItemRow(history: HistoryChat) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = history.title, fontSize = 16.sp, color = ChatGPTTextDark, modifier = Modifier.weight(1f))
        Text(text = history.time, fontSize = 14.sp, color = ChatGPTHintGray)
        Spacer(modifier = Modifier.width(8.dp))
        Icon(Icons.Default.Star, "Star", tint = ChatGPTBlue, modifier = Modifier.size(18.dp))
    }
}

@Composable
fun ChatBubbleRow(message: ChatMessage) {
    val isUser = message.isUser
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            if (!isUser) {
                // AI ရဲ့ စက်ဝိုင်းအဝိုင်း Icon လေး
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(ChatGPTBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text("AI", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            Column {
