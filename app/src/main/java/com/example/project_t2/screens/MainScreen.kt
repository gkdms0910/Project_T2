package com.example.project_t2.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_t2.R
import com.example.project_t2.graphics.RenderImage

import com.example.project_t2.graphics.cloudImage
import com.example.project_t2.graphics.sunImage
import com.example.project_t2.ui.theme.MainFont

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val cloud = cloudImage()
    val sun = sunImage()
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.paper_texture),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x807cccf4), // semi-transparent
                            Color(0x80FFFFFF)
                        )
                    )
                ),
            //contentAlignment = Alignment.Center
        ) {
            RenderImage(
                sun, modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .offset(x = 210.dp, y = -50.dp)
            )
            RenderImage(
                cloud, modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .offset(x = 10.dp, y = 300.dp)
            )
            RenderImage(
                cloud, modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .offset(x = 180.dp, y = 600.dp)
            )
            Text(
                text = "대체로 맑음",
                fontSize = 50.sp,
                //fontWeight = FontWeight.Bold,
                fontFamily = MainFont,
                color = Color(0xFF333333),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 200.dp)
                    .align(Alignment.TopCenter)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewMainScreen() {
    MainScreen()
}