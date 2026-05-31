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
import androidx.compose.material.icons.filled.KeyboardVoice
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

// ChatGPT UI Light Theme Palette (မူလအတိုင်း)
val ChatGPTBlue = Color(0xFF0D6EFD)       
val ChatGPTLightBg = Color(0xFFFFFFFF)    
val ChatGPTTextDark = Color(0xFF111111)   
val ChatGPTHintGray = Color(0xFF6E6E80)   
val ChatGPTBubbleAI = Color(0xFFF7F7F8)   
val ChatGPTBorder = Color(0xFFE5E5E5)     

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
            .baseUrl("https://api.openai.com/") // API ချိတ်ဆက်မှု မှန်ကန်စေရန် Base URL ပြင်ဆင်ချက်
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
                item {
                    Text(
                        text = "Chats",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = ChatGPTTextDark,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }

                items(viewModel.chatHistory) { history ->
                    HistoryItemRow(history)
                    HorizontalDivider(color = ChatGPTBorder, thickness = 0.5.dp)
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

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

            BottomInputBar(
                text = inputMessage,
                onTextChange = { inputMessage = it },
                onSendClick = {
                    if (inputMessage.isNotBlank()) {
                        val currentText = inputMessage
                        inputMessage = ""
                        viewModel.sendMessage(currentText) {
                            // On complete logic
                        }
                    }
                }
            )
        }
    }
}

// ======= [4. UI COMPONENT LISTS WITH PROPER BRACKETS] =======

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
                if (!isUser) {
                    Text(
                        text = "AI Assistant", 
                        fontSize = 14.sp, 
                        fontWeight = FontWeight.Bold, 
                        color = ChatGPTBlue,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
                
                // 🛠️ ဒီနေရာမှစ၍ ပြတ်တောက်သွားသော မူလ Card ကုဒ်ကို ပိတ်ကွင်းများစနစ်တကျဖြင့် ပြန်ဆက်ပေးထားပါသည်
                Card(
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 2.dp,
                        bottomEnd = if (isUser) 2.dp else 16.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isUser) ChatGPTBlue else ChatGPTBubbleAI
                    ),
                    modifier = Modifier.animateContentSize()
                ) {
                    Text(
                        text = message.text,
                        color = if (isUser) Color.White else ChatGPTTextDark,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }
            }
        }
    }
}

// 🛠️ ကုဒ်အပေါ်ပိုင်းမှာ ခေါ်သုံးထားသော်လည်း အောက်ခြေတွင် ကျန်ရစ်ခဲ့သော မူလ Component အား ဖြည့်စွက်ခြင်း
@Composable
fun BottomInputBar(
    text: String,
    onTextChange = (String) -> Unit,
    onSendClick = () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(ChatGPTLightBg),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = { Text("Message...", color = ChatGPTHintGray) },
            modifier = Modifier
                .weight(1f)
                .border(1.dp, ChatGPTBorder, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = ChatGPTTextDark,
                unfocusedTextColor = ChatGPTTextDark
            ),
            trailingIcon = {
                IconButton(onClick = { /* Voice Dictation Logic */ }) {
                    Icon(Icons.Default.KeyboardVoice, contentDescription = "Voice", tint = ChatGPTHintGray)
                }
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = onSendClick,
            modifier = Modifier
                .size(48.dp)
                .background(ChatGPTBlue, CircleShape)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Send",
                tint = Color.White
            )
        }
    }
}
