package com.example.project_t2.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_t2.R
import com.example.project_t2.graphics.ImageSpec
import com.example.project_t2.graphics.RenderImage
import com.example.project_t2.graphics.paperTexture
import com.example.project_t2.graphics.paperTexture2

@Composable
fun DiaryScreen(modifier: Modifier = Modifier) {
    //val paper = paperTexture()


    Box(modifier = modifier.fillMaxSize()) {
        // 배경 이미지 (맨 아래에 위치)
        Image(
            painter = painterResource(id = R.drawable.paper_texture),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Column으로 정렬
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            // 상단 툴바
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = com.example.project_t2.R.drawable.outline_menu_24),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp)
                        .size(50.dp)
                )
                Image(
                    painter = painterResource(id = com.example.project_t2.R.drawable.outline_check_circle_24),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                        .size(50.dp)
                )
            }

            // 날씨
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f)
                    .background(Color(0x66FFC0C0)),
                contentAlignment = Alignment.Center
            ) {
                Text("날씨 정보", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            // 감정
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .background(Color(0x7EC0F0C0)),
                contentAlignment = Alignment.Center
            ) {
                Text("감정 이모티콘", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            // 일기 작성 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(6f)
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                Text("일기 작성 영역", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            // 글쓰기 도구
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0x6180FF80)),
                contentAlignment = Alignment.Center
            ) {
                Text("글쓰기 도구", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}

@Preview
@Composable
private fun PreviewDiaryScreen() {
    DiaryScreen()
}