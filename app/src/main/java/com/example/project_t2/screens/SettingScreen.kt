package com.example.project_t2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.project_t2.models.AppBackground
import com.example.project_t2.models.GenericTopAppBar
import com.example.project_t2.ui.theme.MainFont

@Composable
fun SettingScreen(navController: NavController) {
    AppBackground {
        Scaffold(
            topBar = {
                GenericTopAppBar(
                    title = "설정",
                    onNavigate = { route -> navController.navigate(route) }
                )
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FontSizeSettingBox()
            }
        }
    }
}

@Composable
fun FontSizeSettingBox() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Text(text = "글자 크기 설정", fontSize = 18.sp, fontFamily = MainFont)
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = { /* 크게 로직 */ }) {
                Text("크게", fontSize = 16.sp, fontFamily = MainFont)
            }
            Button(onClick = { /* 중간 로직 */ }) {
                Text("중간", fontSize = 16.sp, fontFamily = MainFont)
            }
            Button(onClick = { /* 작게 로직 */ }) {
                Text("작게", fontSize = 16.sp, fontFamily = MainFont)
            }
        }
    }
}