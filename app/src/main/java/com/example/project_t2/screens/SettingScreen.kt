package com.example.project_t2.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_t2.R

@Composable
fun SettingScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        // 배경 이미지
        Image(
            painter = painterResource(id = R.drawable.paper_texture),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 상단 메뉴 (햄버거 버튼 자리)
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                MenuDropdown(navController = navController)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "폰트 크기 변경",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = { /* TODO: Set large font size */ }) {
                    Text("크게")
                }
                Button(onClick = { /* TODO: Set medium font size */ }) {
                    Text("중간")
                }
                Button(onClick = { /* TODO: Set small font size */ }) {
                    Text("작게")
                }
            }

        }
    }
}

@Preview
@Composable
private fun PreviewSearchScreen() {
    SettingScreen(navController = rememberNavController()) // 프리뷰용 NavController
}