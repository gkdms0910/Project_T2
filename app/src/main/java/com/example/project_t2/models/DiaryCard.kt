package com.example.project_t2.models

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "제목: ${diary.title}", fontWeight = FontWeight.Bold, fontFamily = MainFont)
            Text(text = "내용: ${diary.content}", maxLines = 1, fontFamily = MainFont)
            Text(text = "날짜: ${diary.time.toLocalDate()}", fontFamily = MainFont)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "감정: ", fontFamily = MainFont)
                Image(
                    painter = painterResource(id = diary.emotion.imageResId),
                    contentDescription = diary.emotion.displayName,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "(${diary.emotion.displayName})", fontFamily = MainFont)
            }
        }
    }
}