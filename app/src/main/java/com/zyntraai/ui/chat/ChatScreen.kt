package com.zyntraai.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

@Composable
fun ChatScreen() {

    var message by remember {
        mutableStateOf("")
    }

    val messages = remember {

        mutableStateListOf(

            ChatMessage(
                "Hello 👋 I am Zyntra AI",
                false
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050816))
    ) {

        Text(
            text = "Zyntra AI",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,

            modifier = Modifier
                .padding(20.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {

            items(messages) { item ->

                MessageBubble(item)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),

            verticalAlignment =
            Alignment.CenterVertically
        ) {

            TextField(
                value = message,

                onValueChange = {
                    message = it
                },

                modifier = Modifier
                    .weight(1f),

                placeholder = {
                    Text("Type Message")
                }
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            FloatingActionButton(
                onClick = {

                    if (message.isNotEmpty()) {

                        messages.add(
                            ChatMessage(
                                message,
                                true
                            )
                        )

                        messages.add(
                            ChatMessage(
                                "AI Reply : $message",
                                false
                            )
                        )

                        message = ""
                    }
                },

                containerColor =
                Color(0xFF2962FF)
            ) {

                Icon(
                    Icons.Default.Send,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun MessageBubble(
    message: ChatMessage
) {

    Row(
        modifier = Modifier.fillMaxWidth(),

        horizontalArrangement =
        if (message.isUser)
            Arrangement.End
        else
            Arrangement.Start
    ) {

        Card(
            modifier = Modifier
                .padding(8.dp)
                .widthIn(max = 280.dp),

            colors =
            CardDefaults.cardColors(

                containerColor =
                if (message.isUser)
                    Color(0xFF2962FF)
                else
                    Color(0xFF111827)
            ),

            shape =
            RoundedCornerShape(16.dp)
        ) {

            Text(
                text = message.text,

                color = Color.White,

                modifier = Modifier
                    .padding(12.dp)
            )
        }
    }
}
