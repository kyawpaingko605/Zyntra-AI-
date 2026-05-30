package com.zyntraai.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.zyntraai.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController
) {

    LaunchedEffect(true) {

        delay(2500)

        navController.navigate(
            Screen.Onboarding.route
        ) {
            popUpTo(0)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050816)),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "Zyntra AI",
            color = Color.White,
            fontSize = 34.sp
        )
    }
}
