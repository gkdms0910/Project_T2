package com.example.project_t2.models

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.project_t2.roomDB.DiaryEntity

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
        // 수정된 부분: 감정 표시를 Image와 Text로 변경
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "감정: ")
            Image(
                painter = painterResource(id = diary.emotion.imageResId),
                contentDescription = diary.emotion.displayName,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "(${diary.emotion.displayName})")
        }
    }
}