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
fun DiaryCard(
    diary: DiaryEntity,
    modifier: Modifier = Modifier, // modifier 파라미터 추가
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp, horizontal = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "제목: ${diary.title}", fontWeight = FontWeight.Bold, fontFamily = MainFont, style = MaterialTheme.typography.titleMedium)
            Text(text = "내용: ${diary.content}", maxLines = 2, fontFamily = MainFont, style = MaterialTheme.typography.bodyMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "날짜: ${diary.time.toLocalDate()}", fontFamily = MainFont, style = MaterialTheme.typography.labelSmall)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "감정: ", fontFamily = MainFont, style = MaterialTheme.typography.labelSmall)
                    Image(
                        painter = painterResource(id = diary.emotion.imageResId),
                        contentDescription = diary.emotion.displayName,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}