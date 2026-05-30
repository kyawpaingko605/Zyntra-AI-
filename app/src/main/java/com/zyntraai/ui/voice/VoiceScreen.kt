package com.zyntraai.ui.voice

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.draw.scale

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VoiceScreen() {

    val infiniteTransition =
        rememberInfiniteTransition(
            label = "pulse"
        )

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.25f,

        animationSpec =
        infiniteRepeatable(
            animation = tween(
                durationMillis = 1000
            ),
            repeatMode =
            RepeatMode.Reverse
        ),

        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050816))
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),

            horizontalAlignment =
            Alignment.CenterHorizontally,

            verticalArrangement =
            Arrangement.Center
        ) {

            Text(
                text = "Zyntra AI",
                color = Color.White,
                fontSize = 28.sp
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Box(
                modifier = Modifier
                    .size(250.dp)
                    .scale(scale)
                    .background(
                        color =
                        Color(0x332962FF),
                        shape =
                        CircleShape
                    )
            )

            Spacer(
                modifier = Modifier.height(40.dp)
            )

            FloatingActionButton(
                onClick = {

                },

                containerColor =
                Color(0xFF2962FF)
            ) {

                Icon(
                    imageVector =
                    Icons.Default.Mic,

                    contentDescription = null,

                    tint = Color.White
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Listening...",
                color = Color.White,
                fontSize = 18.sp
            )
        }
    }
}
