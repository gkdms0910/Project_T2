package com.example.project_t2.data

import com.example.Project_T2.data.Weather.Response.Body.Items.Item
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

suspend fun GetWeather(): List<Item> {
    val current = LocalDateTime.now()
    val baseDate = current.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
    val baseTime = String.format("%02d00", if (current.minute < 40) current.hour - 1 else current.hour)

    val response = getUltraSrtNcst(  // API 호출 함수
        baseDate = baseDate,
        baseTime = baseTime,
        nx = 55, // 현재 위치, 일단 서울로 고정해두었고, 설정 화면에서 변경할 수 있게 수정 예정
        ny = 127
    )

    return response?.body?.items?.item
        ?.filter { it.category in listOf("RN1", "SKY", "T1H", "UUU", "VVV", "REH", "PTY", "LGT", "WSD") }
        ?.groupBy { it.category }
        ?.mapValues { entry ->
            entry.value.minByOrNull { item ->
                item?.fcstDate?.toIntOrNull()?.times(10000)!! + (item.fcstTime?.toIntOrNull() ?: 0)
            }
        }
        ?.values
        ?.filterNotNull()
        ?: emptyList()
}