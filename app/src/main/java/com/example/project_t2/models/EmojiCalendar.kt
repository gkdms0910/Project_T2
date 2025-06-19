package com.example.p2w.model

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.p2w.graphics.Emotion
import com.example.p2w.models.Weathers
import com.example.p2w.roomDB.DiaryEntity
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

@Composable
fun EmojiCalendar(
    yearMonth: YearMonth,
    emojiMap: Map<LocalDate, String>,
    onDateClick: (LocalDate) -> Unit
) {
    val days = buildCalendarDays(yearMonth)
    val today = LocalDate.now()

    // 요일 표시
    val dayLabels = listOf("일", "월", "화", "수", "목", "금", "토")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        dayLabels.forEachIndexed { index, label ->
            val textColor = when (index) {
                0 -> Color(0xFFD32F2F)  // 일요일 빨간 칠
                6 -> Color(0xFF42A5F5)  // 토요일 파란 칠
                else -> MaterialTheme.colorScheme.onSurface
            }
            Text(
                text = label,
                color = textColor,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f),
                fontSize = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }

    // 날짜 칸
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.height(350.dp)
    ) {
        items(days) { date ->
            if (date == null) {
                Box(modifier = Modifier.aspectRatio(1f)) {}  // 1일 앞의 빈 칸
            } else {
                val isToday = date == today
                val emoji = emojiMap[date]

                val textColor = when (date.dayOfWeek) {
                    DayOfWeek.SUNDAY   -> Color(0xFFD32F2F)
                    DayOfWeek.SATURDAY -> Color(0xFF42A5F5)
                    else               -> MaterialTheme.colorScheme.onSurface
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
                    Text(
                        text = emoji ?: date.dayOfMonth.toString(),
                        fontSize = if (emoji != null) 20.sp else 14.sp,
                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                        color = textColor
                    )
                }
            }
        }
    }
}

fun buildCalendarDays(month: YearMonth): List<LocalDate?> {
    val firstDay = month.atDay(1)
    val lastDay = month.atEndOfMonth()
    val dayOfWeekOffset = firstDay.dayOfWeek.value % 7 // 일요일 = 0
    val days = mutableListOf<LocalDate?>()

    // 앞쪽 빈 칸
    repeat(dayOfWeekOffset) {
        days.add(null)
    }

    // 실제 날짜
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
            Emotion.ANGRY,
            "내용2",
            LocalDateTime.of(2025, 6, 6, 18, 30)
        )
    )
    CalendarWithMonthNavigation(
        diaryList = previewList,
        onDateClick = {}
    )
}