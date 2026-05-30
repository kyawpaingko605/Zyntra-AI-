package com.zyntraai // 👈 Package နာမည်ကို com.zyntraai သို့ လဲလှယ်ထားပါသည်

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountCircle
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
import dev.jeziellago.compose.markdown.MarkdownText 
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

// Zyntra Color Palette
val ZyntraPrimary = Color(0xFF7C4DFF) 
val ZyntraBackground = Color(0xFF14121F)
val ZyntraSurface = Color(0xFF211D36)
val ZyntraBubbleAI = Color(0xFF2A244D)

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

// ======= [2. ARCHITECTURE: STATE & BUSINESS LOGIC] =======

data class ChatMessage(val id: String, val text: String, val isUser: Boolean)

class ChatViewModel : ViewModel() {
    var messages by mutableStateOf(listOf<ChatMessage>())
    var isLoading by mutableStateOf(false)

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
                
                val response = RetrofitClient.instance.getChatCompletion(
                    apiKey = apiKey,
                    request = OpenAIRequest(messages = apiMessages)
                )
                
                val aiResponseText = response.choices.firstOrNull()?.message?.content 
                    ?: "Error: အဖြေမရရှိပါ။"
                
                messages = messages + ChatMessage(id = System.nanoTime().toString(), text = aiResponseText, isUser = false)
                
            } catch (e: Exception) {
                messages = messages + ChatMessage(
                    id = System.nanoTime().toString(), 
                    text = "Zyntra AI နှင့် ချိတ်ဆက်မှု မအောင်မြင်ပါ: ${e.localizedMessage}", 
                    isUser = false
                )
            } finally {
                isLoading = false
                onComplete()
            }
        }
    }
}

// ======= [3. UI DESIGN: PRODUCTION UI LAYER] =======

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val myViewModel: ChatViewModel = viewModel()
                ZyntraMainScreen(myViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZyntraMainScreen(viewModel: ChatViewModel) {
    var inputMessage by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, "AI", tint = ZyntraPrimary, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Zyntra AI", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ZyntraSurface)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(ZyntraBackground)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 8.dp)
            ) {
                items(viewModel.messages, key = { it.id }) { message ->
                    ChatRowItem(message = message)
                }
                if (viewModel.isLoading) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = ZyntraPrimary)
                        }
                    }
                }
            }

            ChatInputRow(
                text = inputMessage,
                onTextChange = { inputMessage = it },
                onSendClick = {
                    if (inputMessage.isNotBlank()) {
                        val currentInput = inputMessage
                        inputMessage = ""
                        viewModel.sendMessage(currentInput) {
                            coroutineScope.launch {
                                if (viewModel.messages.isNotEmpty()) {
                                    listState.animateScrollToItem(viewModel.messages.size - 1)
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ChatRowItem(message: ChatMessage) {
    val rowBgColor = if (message.isUser) ZyntraBackground else ZyntraBubbleAI
    Row(
        modifier = Modifier.fillMaxWidth().background(rowBgColor).padding(vertical = 16.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        if (message.isUser) {
            Icon(Icons.Default.AccountCircle, "User", tint = Color.LightGray, modifier = Modifier.size(32.dp))
        } else {
            Box(modifier = Modifier.size(32.dp).clip(RoundedCornerShape(4.dp)).background(ZyntraPrimary), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Star, "AI", tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Box(modifier = Modifier.weight(1f).animateContentSize()) {
            MarkdownText(markdown = message.text, color = Color(0xFFECECF1), fontSize = 16.sp, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun ChatInputRow(text: String, onTextChange: (String) -> Unit, onSendClick: () -> Unit) {
    Surface(color = ZyntraSurface) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = text,
                onValueChange = onTextChange,
                placeholder = { Text("Message Zyntra...", color = Color.Gray) },
                modifier = Modifier.weight(1f).clip(RoundedCornerShape(24.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF2E294E),
                    unfocusedContainerColor = Color(0xFF2E294E),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 4
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onSendClick,
                modifier = Modifier.clip(CircleShape).background(if (text.isNotBlank()) ZyntraPrimary else Color.Transparent)
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, "Send", tint = if (text.isNotBlank()) Color.White else Color.Gray)
            }
        }
    }
}
