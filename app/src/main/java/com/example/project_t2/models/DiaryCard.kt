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
import com.example.project_t2.ui.theme.MainFont

@Composable
fun DiaryCard(diary: DiaryEntity, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = "제목: ${diary.title}", fontWeight = FontWeight.Bold, fontFamily = MainFont)
        Text(text = "내용: ${diary.content}", maxLines = 1, fontFamily = MainFont)
        Text(text = "날짜: ${diary.time.toLocalDate()}", fontFamily = MainFont)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "감정: ", fontFamily = MainFont)
            Image(
                painter = painterResource(id = diary.emotion.imageResId),
                contentDescription = diary.emotion.displayName,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "(${diary.emotion.displayName})", fontFamily = MainFont)
        }
    }
}