package com.example.project_t2.models

import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import com.example.project_t2.graphics.Emotion
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.time.LocalDate

class SingleEmotionDecorator(
    private val emotionByDate: Map<LocalDate, Emotion>
) : DayViewDecorator {
    private var currentEmoji: String? = null

    override fun shouldDecorate(day: CalendarDay): Boolean {
        val date = LocalDate.of(day.year, day.month, day.day)
        currentEmoji = emotionByDate[date]?.emoji
        return currentEmoji != null
    }

    override fun decorate(view: DayViewFacade) {
        val emoji = currentEmoji ?: return
        val spannable = SpannableString(emoji)
        spannable.setSpan(RelativeSizeSpan(1.5f), 0, spannable.length, 0)
        view.addSpan { spannable }
        view.setDaysDisabled(true)
    }

}