package com.example.p2w.model

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.p2w.roomDB.DiaryEntity

@Composable
fun DiaryCard(diary: DiaryEntity, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = "제목: ${diary.title}", fontWeight = FontWeight.Bold)
        Text(text = "내용: ${diary.content}", maxLines = 1)
        Text(text = "날짜: ${diary.time.toLocalDate()}")
        Text(text = "감정: ${diary.emotion.emoji} (${diary.emotion.displayName})")
    }
}