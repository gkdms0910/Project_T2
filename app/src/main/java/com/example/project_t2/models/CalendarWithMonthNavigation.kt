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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_t2.graphics.Emotion
import com.example.project_t2.roomDB.DiaryEntity
import com.example.project_t2.ui.theme.MainFont
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

fun convertDiaryToImageMap(diaryList: List<DiaryEntity>): Map<LocalDate, Int> {
    return diaryList.associate { diary ->
        diary.time.toLocalDate() to diary.emotion.imageResId
    }
}

@Composable
fun CalendarWithMonthNavigation(
    diaryList: List<DiaryEntity>,
    onDateClick: (DiaryEntity) -> Unit
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val imageMap = convertDiaryToImageMap(diaryList)

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
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
                color = MaterialTheme.colorScheme.primary,
                fontFamily = MainFont
            )

            Text(
                text = "${
                    currentMonth.month.getDisplayName(
                        TextStyle.FULL,
                        Locale.KOREAN // Locale을 한국으로 명시
                    )
                } ${currentMonth.year}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MainFont
            )

            Text(
                text = "다음달 >",
                modifier = Modifier.clickable {
                    currentMonth = currentMonth.plusMonths(1)
                },
                color = MaterialTheme.colorScheme.primary,
                fontFamily = MainFont
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        DiaryCalendarGrid(
            yearMonth = currentMonth,
            imageMap = imageMap,
            onDateClick = { clickedDate ->
                val diary = diaryList.find { it.time.toLocalDate() == clickedDate }
                diary?.let { onDateClick(it) }
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