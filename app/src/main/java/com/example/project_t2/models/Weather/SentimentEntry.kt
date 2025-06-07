package com.example.project_t2.models.Weather

import com.example.project_t2.models.Sentiments
import com.example.project_t2.models.Weathers
import kotlinx.serialization.Serializable

@Serializable
data class SentimentEntry(
    val temperature: Float,
    val humidity: Float,
    val weather: Weathers,
    val sunshine: Float,
    val wind: Float,
    val pm10: Float,
    val rainfall: Float,
    var sentiment: Sentiments
)
