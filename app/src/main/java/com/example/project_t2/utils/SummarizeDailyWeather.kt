package com.example.project_t2.utils

import com.example.project_t2.models.Sentiments
import com.example.project_t2.models.Weather.DailyWeather
import com.example.project_t2.models.Weather.SentimentEntry
import com.example.project_t2.models.Weathers

fun summarizeDailyWeather(dailyWeather: DailyWeather): SentimentEntry {
    val avgTemp = dailyWeather.hourlyWeather.map { it.temperature }.average().toFloat()
    val avgHumidity = dailyWeather.hourlyWeather.map { it.humidity }.average().toFloat()
    val avgSunshine = dailyWeather.hourlyWeather.map { it.sunshine }.average().toFloat()
    val avgWind = dailyWeather.hourlyWeather.map { it.wind }.average().toFloat()
    val avgPm10 = dailyWeather.hourlyWeather.map { it.pm10 }.average().toFloat()
    val avgRainfall = dailyWeather.hourlyWeather.map { it.rainfall }.average().toFloat()
    val mostWeather = dailyWeather.hourlyWeather
        .groupingBy { it.weather }
        .eachCount()
        .maxByOrNull { it.value }
        ?.key ?: Weathers.SUNNY

    return SentimentEntry(
        temperature = avgTemp,
        humidity = avgHumidity,
        weather = mostWeather,
        sunshine = avgSunshine,
        wind = avgWind,
        pm10 = avgPm10,
        rainfall = avgRainfall,
        sentiment = Sentiments.HAPPY // Default Sentiment
    )
}