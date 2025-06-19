package com.example.project_t2.data

import android.util.Log

data class WeatherData(val sky: Int?, val pty: Int?, val t1h: Double?)

object WeatherAnalyzer {
    fun analyze(data: WeatherData): String {
        Log.d("WeatherAnalyzer", "pty: ${data.pty}, sky: ${data.sky}, t1h: ${data.t1h}")

        return when {
            data.pty == 1 || data.pty == 2 -> "비"  // 1: 비, 2: 비/눈
            data.sky == 3 || data.sky == 4 -> "흐림"
            data.sky == 1 -> "맑음"
            else -> "날씨정보를 불러오지 못했어요"
        }
    }
}