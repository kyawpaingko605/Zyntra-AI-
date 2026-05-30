package com.zyntraai.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050816))
            .padding(20.dp),

        horizontalAlignment =
        Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        Surface(
            modifier = Modifier.size(120.dp),
            shape = CircleShape,
            color = Color(0xFF2962FF)
        ) {

            Box(
                contentAlignment =
                Alignment.Center
            ) {

                Icon(
                    imageVector =
                    Icons.Default.Person,

                    contentDescription = null,

                    tint = Color.White,

                    modifier =
                    Modifier.size(70.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Zyntra User",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Premium Member",
            color = Color(0xFFFFD700)
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        ProfileItem(
            icon = Icons.Default.Settings,
            title = "Settings"
        )

        ProfileItem(
            icon = Icons.Default.Notifications,
            title = "Notifications"
        )

        ProfileItem(
            icon = Icons.Default.Star,
            title = "Subscription"
        )

        ProfileItem(
            icon = Icons.Default.Info,
            title = "About App"
        )

        ProfileItem(
            icon = Icons.Default.ExitToApp,
            title = "Logout"
        )
    }
}

@Composable
fun ProfileItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),

        colors = CardDefaults.cardColors(
            containerColor =
            Color(0xFF111827)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),

            verticalAlignment =
            Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF2962FF)
            )

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}
