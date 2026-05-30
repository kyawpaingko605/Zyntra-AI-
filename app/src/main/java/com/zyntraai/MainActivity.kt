package com.zyntraai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

// ပုံထဲကအတိုင်း Premium ဆန်သော Futuristic Dark Theme Palette
val ZyntraPrimary = Color(0xFF2979FF)       // Glowing Cyan Blue
val ZyntraBackground = Color(0xFF070913)    // Deep Premium Dark Navy
val ZyntraSurface = Color(0xFF0F1326)       // Input Box Border & Container
val ZyntraTextGray = Color(0xFF8F94A8)      // Subtitles Text Color

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
            .baseUrl("https://api.openai.com/")
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
                    Text(
                        text = "Speaking To AI Bot", 
                        color = Color.White, 
                        fontSize = 18.sp, 
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Back handle */ }) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.08s)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                                contentDescription = "Back", 
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ZyntraBackground)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(ZyntraBackground),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            // ၁။ အပေါ်ဘက် စကားပြောစာတန်း (Status text)
            Text(
                text = "Go ahead, I'm listening...",
                color = ZyntraTextGray,
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 28.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ၂။ AI စဉ်းစားနေချိန်တွင် Message List ပြသရန် (တက်လာသော စာသားများအား စောင့်ကြည့်ရန်)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                if (viewModel.messages.isEmpty() && !viewModel.isLoading) {
                    // စကားမပြောသေးခင် အလယ်တွင် ပြသမည့် Glowing Light (ပုံထဲကအတိုင်း)
                    VoiceGlowingCircle()
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        items(viewModel.messages, key = { it.id }) { message ->
                            Text(
                                text = message.text,
                                color = if (message.isUser) ZyntraPrimary else Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                            )
                        }
                        if (viewModel.isLoading) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp), 
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = ZyntraPrimary, strokeWidth = 2.dp)
                                }
                            }
                        }
                    }
                }
            }

            // ၃။ ပုံထဲကအတိုင်း Suggestion Box (ဥပမာ စာသားပြသသည့်နေရာ)
            if (viewModel.messages.isEmpty()) {
                Text(
                    text = "Tell me the simple timeline of human\nhistory based on the book Sapiens.",
                    color = ZyntraTextGray.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)
                )
            }

            // ၄။ အောက်ခြေ Control Area (စာရိုက်ဘား နှင့် အသံဖမ်းခလုတ်များ တွဲဖက်တည်ဆောက်မှု)
            Surface(
                color = Color.Transparent,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
