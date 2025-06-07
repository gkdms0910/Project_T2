package com.example.project_t2.models

import com.example.project_t2.graphics.Emotion
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.time.LocalDate

class EmotionDecorator {
    fun applyDecorators(
        calendarView: MaterialCalendarView,
        emotionByDate: Map<LocalDate, Emotion>
    ) {
        for ((localDate, emotion) in emotionByDate) {
            val calendarDay = CalendarDay.from(
                localDate.year,
                localDate.monthValue,
                localDate.dayOfMonth
            )
            calendarView.addDecorator(
                SingleEmotionDecorator(mapOf(localDate to emotion))
            )
        }
    }
}

