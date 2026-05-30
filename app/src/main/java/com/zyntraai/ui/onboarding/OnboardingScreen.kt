package com.zyntraai.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController

import com.zyntraai.R
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

        Image(
            painter = painterResource(
                id = R.drawable.robot
            ),
            contentDescription = null,

            modifier = Modifier
                .size(280.dp),

            contentScale =
            ContentScale.Fit
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Text(
            text =
            "Providing The Best\nAI Solutions",

            color = Color.White,

            fontSize = 32.sp,

            fontWeight =
            FontWeight.Bold,

            textAlign =
            TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text =
            "Experience next generation artificial intelligence with Zyntra AI",

            color = Color.Gray,

            textAlign =
            TextAlign.Center,

            fontSize = 16.sp
        )

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        Button(
            onClick = {

                navController.navigate(
                    Screen.Home.route
                )
            },

            colors =
            ButtonDefaults.buttonColors(
                containerColor =
                Color(0xFF2962FF)
            ),

            shape =
            RoundedCornerShape(16.dp),

            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
        ) {

            Text(
                text = "Get Started",
                fontSize = 18.sp
            )
        }
    }
}
