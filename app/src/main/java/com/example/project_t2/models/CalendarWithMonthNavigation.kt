package com.example.project_t2.models

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_t2.graphics.Emotion
import com.example.project_t2.roomDB.DiaryEntity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

fun convertDiaryToEmojiMap(diaryList: List<DiaryEntity>): Map<LocalDate, String> {
    return diaryList.associate { diary ->
        diary.time.toLocalDate() to diary.emotion.emoji
    }
}

@Composable
fun CalendarWithMonthNavigation(
    diaryList: List<DiaryEntity>,
    onDateClick: (DiaryEntity) -> Unit
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val emojiMap = convertDiaryToEmojiMap(diaryList)

    Column(modifier = Modifier.padding(16.dp)) {
        // 상단 월 네비게이션
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "< 이전달",
                modifier = Modifier.clickable {
                    currentMonth = currentMonth.minusMonths(1)
                },
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "${
                    currentMonth.month.getDisplayName(
                        TextStyle.FULL,
                        Locale.getDefault()
                    )
                } ${currentMonth.year}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "다음달 >",
                modifier = Modifier.clickable {
                    currentMonth = currentMonth.plusMonths(1)
                },
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        EmojiCalendar(
            yearMonth = currentMonth,
            emojiMap = emojiMap,
            onDateClick = { clickedDate ->
                val diary = diaryList.find { it.time.toLocalDate() == clickedDate }
                diary?.let { onDateClick(it) } // ← 일기가 있을 때만 호출
            }

        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarNavigationPreview() {
    val previewList = listOf(
        DiaryEntity("제목", Weathers.SUNNY, Emotion.HAPPY, "내용", LocalDateTime.of(2025, 6, 5, 12, 0)),
        DiaryEntity("제목2", Weathers.RAINY, Emotion.ANGRY, "내용2", LocalDateTime.of(2025, 6, 6, 18, 30))
    )
    CalendarWithMonthNavigation(
        diaryList = previewList,
        onDateClick = {}
    )
}

