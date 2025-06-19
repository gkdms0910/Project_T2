package com.example.project_t2.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_t2.R
import com.example.project_t2.graphics.Emotion

@Composable
fun DiaryScreen(modifier: Modifier = Modifier) {
    // 1. 상태 관리: 제목, 내용, 감정을 저장할 변수
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedEmotion by remember { mutableStateOf<Emotion?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
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
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 2. 상단 툴바 (메뉴, 저장 버튼)
            DiaryTopAppBar(
                onMenuClick = { /* TODO: 메뉴 클릭 시 동작 */ },
                onSaveClick = { /* TODO: 저장 로직 (ViewModel 호출) */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. 제목 입력 필드
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("제목") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4. 감정 선택 영역
            EmotionSelector(
                selectedEmotion = selectedEmotion,
                onEmotionSelected = { selectedEmotion = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 5. 내용 입력 필드
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("오늘의 이야기를 들려주세요.") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // 남은 공간을 모두 차지
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun DiaryTopAppBar(onMenuClick: () -> Unit, onSaveClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.outline_menu_24),
            contentDescription = "Menu",
            modifier = Modifier
                .size(50.dp)
                .clickable { onMenuClick() }
        )
        Text("일기 작성", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Image(
            painter = painterResource(id = R.drawable.outline_check_circle_24),
            contentDescription = "Save",
            modifier = Modifier
                .size(50.dp)
                .clickable { onSaveClick() }
        )
    }
}

@Composable
fun EmotionSelector(selectedEmotion: Emotion?, onEmotionSelected: (Emotion) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Emotion.values().forEach { emotion ->
            val isSelected = emotion == selectedEmotion
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) Color.LightGray.copy(alpha = 0.5f) else Color.Transparent)
                    .clickable { onEmotionSelected(emotion) },
                contentAlignment = Alignment.Center
            ) {
                Text(text = emotion.emoji, fontSize = 32.sp)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewDiaryScreen() {
    DiaryScreen()
}