package com.example.project_t2.utils

import com.example.project_t2.data.Weather.Response.Body.Items.Item
import com.example.project_t2.models.Sentiments
import com.example.project_t2.models.Weathers
import com.example.project_t2.roomDB.WSentiment.WSentimentEntity
import java.time.LocalDate
import java.time.LocalTime

fun getWeatherParser(list: List<Item>): WSentimentEntity {
    val map = list.associateBy { it.category }

    val temperature = map["T1H"]?.fcstValue?.toFloatOrNull() ?: 0f
    val humidity = map["REH"]?.fcstValue?.toFloatOrNull() ?: 0f
    val wind = map["WSD"]?.fcstValue?.toFloatOrNull() ?: 0f
    val rainfall = map["RN1"]?.fcstValue?.toFloatOrNull() ?: 0f

    val date = LocalDate.now().toString()
    val time = LocalTime.now().hour

    return WSentimentEntity(
        temperature = temperature,
        humidity = humidity,
        wind = wind,
        rainfall = rainfall,
        sentiment = Sentiments.NONE,
        date = date,
        time = time,
        hours = 1
    )
}