package com.example.project_t2.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.project_t2.graphics.RenderImage
import com.example.project_t2.graphics.titleText
import com.example.project_t2.models.AppBackground
import com.example.project_t2.ui.theme.MainFont

@Composable
fun MainScreen(modifier: Modifier = Modifier, navController: NavController) {
    AppBackground {
        Box(
            modifier = modifier
                .fillMaxSize()
                .clickable {
                    navController.navigate("diary")
                },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                RenderImage(
                    titleText(), modifier = Modifier
                        .fillMaxWidth(0.7f)
                )
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "화면을 터치하여 일기 작성하기",
                    fontFamily = MainFont,
                    fontSize = 20.sp
                )
            }
        }
    }
}