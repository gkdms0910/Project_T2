package com.example.project_t2.data
data class WeatherData(val sky: Int?, val rn1: Double?, val t1h: Double?)

object WeatherAnalyzer {
    fun analyze(data: WeatherData): String {
        return when {
            data.rn1 != null && data.rn1 > 0.0 -> "비"
            data.sky != null && data.sky >= 7 -> "흐림"
            data.sky != null && data.sky >= 5 -> "구름 많음"
            else -> "맑음"
        }
    }
}