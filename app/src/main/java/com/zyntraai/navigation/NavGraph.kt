package com.zyntraai.navigation

import androidx.compose.runtime.Composable
import com.zyntraai.ui.voice.VoiceScreen
import androidx.navigation.compose.*
import com.zyntraai.ui.chat.ChatScreen
import com.zyntraai.ui.home.HomeScreen
import com.zyntraai.ui.onboarding.OnboardingScreen
import com.zyntraai.ui.splash.SplashScreen
import com.zyntraai.ui.profile.ProfileScreen
@Composable
fun NavGraph() {

    val navController =
        rememberNavController()

    NavHost(
        navController = navController,
        startDestination =
        Screen.Splash.route
    ) {

        composable(
            Screen.Splash.route
        ) {

            SplashScreen(navController)
        }

        composable(
            Screen.Onboarding.route
        ) {

            OnboardingScreen(navController)
        }

        composable(
            Screen.Home.route
        ) {

            HomeScreen()
        }
    }
}
