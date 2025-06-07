package com.example.project_t2.models.Weather

import com.example.project_t2.models.Weathers

data class HourlyWeather(
    val temperature: Float,
    val humidity: Float,
    val weather: Weathers,
    val sunshine: Float,
    val wind: Float,
    val pm10: Float,
    val rainfall: Float,
)
