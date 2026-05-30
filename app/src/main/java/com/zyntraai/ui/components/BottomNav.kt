package com.zyntraai.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController

import androidx.navigation.compose.currentBackStackEntryAsState

import com.zyntraai.navigation.Screen

@Composable
fun BottomNavBar(
    navController: NavHostController
) {

    val backStackEntry =
        navController.currentBackStackEntryAsState()

    val currentRoute =
        backStackEntry.value?.destination?.route

    NavigationBar {

        NavigationBarItem(
            selected =
            currentRoute == Screen.Home.route,

            onClick = {

                navController.navigate(
                    Screen.Home.route
                )
            },

            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = null
                )
            },

            label = {
                Text("Home")
            }
        )

        NavigationBarItem(
            selected =
            currentRoute == Screen.Chat.route,

            onClick = {

                navController.navigate(
                    Screen.Chat.route
                )
            },

            icon = {
                Icon(
                    Icons.Default.Chat,
                    contentDescription = null
                )
            },

            label = {
                Text("Chat")
            }
        )

        NavigationBarItem(
            selected =
            currentRoute == Screen.Voice.route,

            onClick = {

                navController.navigate(
                    Screen.Voice.route
                )
            },

            icon = {
                Icon(
                    Icons.Default.Mic,
                    contentDescription = null
                )
            },

            label = {
                Text("Voice")
            }
        )

        NavigationBarItem(
            selected =
            currentRoute == Screen.Profile.route,

            onClick = {

                navController.navigate(
                    Screen.Profile.route
                )
            },

            icon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null
                )
            },

            label = {
                Text("Profile")
            }
        )
    }
}
