package com.zyntraai.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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

data class DashboardItem(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun HomeScreen() {

    val items = listOf(
        DashboardItem(
            "AI Chat",
            "Talk with AI",
            Icons.Default.Chat
        ),
        DashboardItem(
            "Image AI",
            "Generate Images",
            Icons.Default.Image
        ),
        DashboardItem(
            "Voice AI",
            "Voice Assistant",
            Icons.Default.Mic
        ),
        DashboardItem(
            "Code AI",
            "Generate Code",
            Icons.Default.Code
        )
    )

    Scaffold(
        containerColor = Color(0xFF050816),

        bottomBar = {

            NavigationBar(
                containerColor = Color(0xFF111827)
            ) {

                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = null
                        )
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(
                            Icons.Default.Chat,
                            contentDescription = null
                        )
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(
                            Icons.Default.Mic,
                            contentDescription = null
                        )
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null
                        )
                    }
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
        ) {

            Text(
                text = "Hello 👋",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Welcome to Zyntra AI",
                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            PremiumBanner()

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "AI Tools",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth()
            ) {

                items(items) { item ->

                    ToolCard(item)
                }
            }
        }
    }
}

@Composable
fun PremiumBanner() {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2962FF)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),

            horizontalArrangement =
            Arrangement.SpaceBetween,

            verticalAlignment =
            Alignment.CenterVertically
        ) {

            Column {

                Text(
                    text = "Premium Plan",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Unlock AI Power",
                    color = Color.White
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Button(
                    onClick = {}
                ) {

                    Text("Upgrade")
                }
            }

            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(60.dp)
            )
        }
    }
}

@Composable
fun ToolCard(
    item: DashboardItem
) {

    Card(
        modifier = Modifier
            .padding(8.dp)
            .height(160.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF111827)
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = Color(0xFF2962FF),
                modifier = Modifier.size(36.dp)
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = item.title,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = item.description,
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}
