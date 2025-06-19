package com.example.project_t2.data

import com.example.project_t2.data.Weather.Response.Body.Items.Item
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

suspend fun GetWeather(): List<Item> {
    val current = LocalDateTime.now()
    // 분이 40분 미만일 경우, 이전 시간대의 예보를 가져옵니다.
    val adjustedCurrent = if (current.minute < 40) current.minusHours(1) else current
    val baseDate = adjustedCurrent.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
    val baseTime = adjustedCurrent.format(DateTimeFormatter.ofPattern("HH00"))

    val repository = WeatherRepository() // WeatherRepository 인스턴스 생성
    val response = repository.getWeather(  // repository의 getWeather 함수 호출
        baseDate = baseDate,
        baseTime = baseTime,
        nx = 55, // 서울 좌표
        ny = 127
    )

    return response?.response?.body?.items?.item
        ?.filter { it.category in listOf("RN1", "SKY", "T1H", "UUU", "VVV", "REH", "PTY", "LGT", "WSD") }
        ?.groupBy { it.category }
        ?.mapValues { entry ->
            entry.value.minByOrNull { item ->
                item.fcstDate.toIntOrNull()?.times(10000)!! + (item.fcstTime?.toIntOrNull() ?: 0)
            }
        }
        ?.values
        ?.filterNotNull()
        ?: emptyList()
}