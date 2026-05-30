package com.zyntraai.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController

import com.zyntraai.navigation.Screen

@Composable
fun OnboardingScreen(
    navController: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050816))
            .padding(24.dp),

        horizontalAlignment =
        Alignment.CenterHorizontally,

        verticalArrangement =
        Arrangement.Center
    ) {

        Text(
            text = "Welcome To Zyntra AI",
            color = Color.White,
            fontSize = 28.sp
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Button(
            onClick = {

                navController.navigate(
                    Screen.Home.route
                )
            }
        ) {

            Text("Get Started")
        }
    }
}
