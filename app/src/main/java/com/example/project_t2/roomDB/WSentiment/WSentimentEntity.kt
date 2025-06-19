package com.example.project_t2.roomDB.WSentiment

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.project_t2.models.Sentiments

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
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
