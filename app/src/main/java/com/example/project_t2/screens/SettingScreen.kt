package com.example.project_t2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project_t2.models.BackgroundViewModel
import com.example.project_t2.models.GenericTopAppBar

@Composable
fun SettingScreen(navController: NavController) {
    val backgroundViewModel: BackgroundViewModel = viewModel()

    Scaffold(
        topBar = {
            GenericTopAppBar(
                title = "설정",
                onNavigate = { route -> navController.navigate(route) }
            )
        }
    ) { innerPadding ->
        val backgroundResId by backgroundViewModel.backgroundResId.collectAsState()
        val painter = painterResource(id = backgroundResId)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painter,
                    contentScale = ContentScale.Crop
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ThemeSettingBox(viewModel = backgroundViewModel)
                FontSizeSettingBox()
            }
        }
        }
    }


@Composable
fun ThemeSettingBox(viewModel: BackgroundViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(vertical = 8.dp)
            .background(color = Color(0xFFF0F0F0), shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(text = "앱 배경 설정", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                viewModel.setBackground(BackgroundViewModel.DEFAULT_BACKGROUND)
            }) {
                Text("배경 A")
            }
            Button(onClick = {
                viewModel.setBackground(BackgroundViewModel.ALT_BACKGROUND)
            }) {
                Text("배경 B")
            }
        }
    }
}

@Composable
fun FontSizeSettingBox() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(vertical = 8.dp)
            .background(color = Color(0xFFF0F0F0), shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(text = "글자 크기 설정", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { /* 크게 */ }) {
                Text("크게", fontSize = 16.sp)
            }
            Button(onClick = { /* 중간 */ }) {
                Text("중간", fontSize = 16.sp)
            }
            Button(onClick = { /* 작게 */ }) {
                Text("작게", fontSize = 16.sp)
            }
        }
    }
}