package com.zyntraai.navigation

import androidx.compose.runtime.Composable
import com.zyntraai.ui.voice.VoiceScreen
import androidx.navigation.compose.*

import com.zyntraai.ui.home.HomeScreen
import com.zyntraai.ui.onboarding.OnboardingScreen
import com.zyntraai.ui.splash.SplashScreen

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
