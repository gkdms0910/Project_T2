package com.example.project_t2.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_t2.R
import com.example.project_t2.graphics.RenderImage

import com.example.project_t2.graphics.cloudImage
import com.example.project_t2.graphics.sunImage
import com.example.project_t2.graphics.titleText
import com.example.project_t2.ui.theme.MainFont

@Composable
fun MainScreen(modifier: Modifier = Modifier, navController: NavController) {
    val cloud = cloudImage()
    val sun = sunImage()
    val title = titleText()

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable {
                navController.navigate("diary")
            }
    ) {
        // 배경 이미지
        Image(
            painter = painterResource(id = R.drawable.paper_texture),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 콘텐츠
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xD37CCCF4),
                            Color(0xA9FFFFFF)
                        )
                    )
                )
        ) {
            // 타이틀 이미지
            RenderImage(
                title, modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.TopCenter)
                    .padding(top = 200.dp)
            )
            // 구름1
            RenderImage(
                cloud, modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .offset(x = 10.dp, y = 300.dp)
            )
            //구름 2
            RenderImage(
                cloud, modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .offset(x = 180.dp, y = 500.dp)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewMainScreen() {
    MainScreen(modifier=Modifier,navController = rememberNavController())

}