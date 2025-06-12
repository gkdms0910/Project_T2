package com.example.project_t2.roomDB.WSentiment

import androidx.room.Entity
import com.example.project_t2.models.Sentiments
import com.example.project_t2.models.Weathers

@Entity(tableName = "WSentimentTable")
data class WSentimentEntity(
    val temperature: Float,
    val humidity: Float,
    val weather: Weathers,
    val sunshine: Float,
    val wind: Float,
    val pm10: Float,
    val rainfall: Float,
    var sentiment: Sentiments,
    val id: Int = 0
)
