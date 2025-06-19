package com.example.project_t2.roomDB.WSentiment

import androidx.room.Entity
import com.example.project_t2.models.Sentiments
import com.example.project_t2.models.Weathers

@Entity(tableName = "WSentimentTable")
data class WSentimentEntity(
    val temperature: Float,
    val humidity: Float,
    val wind: Float,
    val rainfall: Float,
    var sentiment: Sentiments,
    var date: String,
    var time: Int = 0,
    var hours: Int = 0,
    val id: Int = 0
)
