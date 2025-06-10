package com.example.project_t2.data

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {
    val weatherState by viewModel.weatherData.collectAsState()
    val current = LocalDateTime.now()
    val baseDate = current.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
    val baseTime = String.format("%02d00", if (current.minute < 40) current.hour - 1 else current.hour)

    // 최초 진입 시 데이터 로딩
    LaunchedEffect(Unit) {
        viewModel.loadWeather(
            baseDate = baseDate,
            baseTime = baseTime,
            nx = 55,
            ny = 127
        )
    }

    val weatherItems = weatherState?.response?.body?.items?.item
        ?.filter { it.category in listOf("RN1", "SKY", "T1H") }
        ?.groupBy { it.category }
        ?.mapValues { entry ->
            entry.value.minByOrNull { item ->
                item?.fcstDate?.toIntOrNull()?.times(10000)?.plus(item.fcstTime?.toIntOrNull() ?: 0) ?: Int.MAX_VALUE
            }
        }
        ?.values
        ?.filterNotNull()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        item {
            Text("초단기예보", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))
        }
        // 값 추출
        val skyValue = weatherItems?.find { it.category == "SKY" }?.fcstValue?.toIntOrNull()
        val rn1Value = weatherItems?.find { it.category == "RN1" }?.fcstValue?.toDoubleOrNull()
        val t1hValue = weatherItems?.find { it.category == "T1H" }?.fcstValue?.toDoubleOrNull()

// 날씨 설명 로직
        val weatherDescription = when {
            rn1Value != null && rn1Value >= 1.0 -> "비"
            skyValue == 3 || skyValue == 4 -> "흐림"
            skyValue == 1 -> "맑음"
            else -> "알 수 없음"
        }
        item {
            Text("Base Date: $baseDate")
            Text("Base Time: $baseTime")
            Text("현재 날씨: $weatherDescription", style = MaterialTheme.typography.bodyLarge)
            Text("현재 기온: $t1hValue", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (!weatherItems.isNullOrEmpty()) {
            items(weatherItems.toList()) { item ->
                Text(
                    text = "카테고리: ${item.category}, 값: ${item.fcstValue}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "예보 시간: ${item.fcstDate} ${item.fcstTime}, 좌표: (${item.nx}, ${item.ny})",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
            item {
                Text("데이터를 불러오는 중입니다...", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
