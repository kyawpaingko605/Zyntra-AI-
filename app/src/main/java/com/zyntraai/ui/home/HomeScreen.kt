@Composable
fun ZyntraHomeScreen() {

    Scaffold(
        containerColor = Color(0xFF050816),
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF0B1020)
            ) {

                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(Icons.Default.Home, null)
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(Icons.Default.Mic, null)
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(Icons.Default.Person, null)
                    }
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {

            Text(
                "Hello, User",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2962FF)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {

                Row(
                    modifier = Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column {
                        Text(
                            "Premium Plan",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Unlock AI Power",
                            color = Color.White
                        )
                    }

                    Icon(
                        Icons.Default.Star,
                        null,
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Features",
                color = Color.White,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2)
            ) {

                items(4) {

                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .height(140.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF10172A)
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Icon(
                                Icons.Default.AutoAwesome,
                                null,
                                tint = Color(0xFF3D7BFF)
                            )

                            Spacer(
                                modifier = Modifier.height(12.dp)
                            )

                            Text(
                                "AI Chat",
                                color = Color.White
                            )

                            Text(
                                "Smart Assistant",
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}
