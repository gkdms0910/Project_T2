package com.example.Project_T2.data

import RetrofitInstance

class WeatherRepository {
    private val api = RetrofitInstance.service

    suspend fun getWeather(
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int
    ): Weather? {
        val response = api.getUltraSrtFcst(
            serviceKey = "jYAlCCyVkmO1pgLOL5/DKox/OQGq/gBSKlhMPIIXfDUPsekoWkXa6byP6qDi18ujjrakQ39bJ3n/32aJ2FloQw==",
            baseDate = baseDate,
            baseTime = baseTime,
            nx = nx,
            ny = ny
        )

        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}