package com.example.project_t2.models

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_t2.graphics.Emotion
import com.example.project_t2.roomDB.DiaryEntity
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

// 수정된 부분: 함수명 변경 및 파라미터 타입 변경
@Composable
fun DiaryCalendarGrid(
    yearMonth: YearMonth,
    imageMap: Map<LocalDate, Int>,
    onDateClick: (LocalDate) -> Unit
) {
    val days = buildCalendarDays(yearMonth)
    val today = LocalDate.now()

    val dayLabels = listOf("일", "월", "화", "수", "목", "금", "토")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        dayLabels.forEachIndexed { index, label ->
            val textColor = when (index) {
                0 -> Color(0xFFD32F2F)
                6 -> Color(0xFF42A5F5)
                else -> MaterialTheme.colorScheme.onSurface
            }
            Text(
                text = label,
                color = textColor,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.height(350.dp)
    ) {
        items(days) { date ->
            if (date == null) {
                Box(modifier = Modifier.aspectRatio(1f)) {}
            } else {
                val isToday = date == today
                // 수정된 부분: emoji를 imageResId로 변경
                val imageResId = imageMap[date]

                val textColor = when (date.dayOfWeek) {
                    DayOfWeek.SUNDAY -> Color(0xFFD32F2F)
                    DayOfWeek.SATURDAY -> Color(0xFF42A5F5)
                    else -> MaterialTheme.colorScheme.onSurface
                }

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(2.dp)
                        .background(
                            color = if (isToday) Color(0xFFE3F2FD) else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable { onDateClick(date) },
                    contentAlignment = Alignment.Center
                ) {
                    // 수정된 부분: imageResId가 있으면 Image, 없으면 Text 표시
                    if (imageResId != null) {
                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = "Emotion",
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = date.dayOfMonth.toString(),
                            fontSize = 14.sp,
                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                            color = textColor
                        )
                    }
                }
            }
        }
    }
}

fun buildCalendarDays(month: YearMonth): List<LocalDate?> {
    val firstDay = month.atDay(1)
    val lastDay = month.atEndOfMonth()
    val dayOfWeekOffset = firstDay.dayOfWeek.value % 7

    val days = mutableListOf<LocalDate?>()

    repeat(dayOfWeekOffset) {
        days.add(null)
    }

    for (day in 1..lastDay.dayOfMonth) {
        days.add(month.atDay(day))
    }

    return days
}

@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
    val previewList = listOf(
        DiaryEntity("제목", Weathers.SUNNY, Emotion.HAPPY, "내용", LocalDateTime.of(2025, 6, 5, 12, 0)),
        DiaryEntity(
            "제목2",
            Weathers.RAINY,
            Emotion.BAD,
            "내용2",
            LocalDateTime.of(2025, 6, 6, 18, 30)
        )
    )
    CalendarWithMonthNavigation(
        diaryList = previewList,
        onDateClick = {}
    )
}