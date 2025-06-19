package com.example.project_t2.network

import com.example.project_t2.data.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// 배포시에는 안전하게 보완 적용 필요
// 공공 데이터 포탈에서 발급 받은 자신만의 API키를 입력해 주세요.
private const val API_KEY = "jYAlCCyVkmO1pgLOL5/DKox/OQGq/gBSKlhMPIIXfDUPsekoWkXa6byP6qDi18ujjrakQ39bJ3n/32aJ2FloQw=="
interface WeatherService {
    @GET("getUltraSrtFcst")
    suspend fun getUltraSrtFcst(
        @Query("serviceKey") serviceKey: String,
        @Query("pageNo") pageNo: Int = 1,
        @Query("numOfRows") numOfRows: Int = 1000,
        @Query("dataType") dataType: String = "JSON",
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String,
        @Query("nx") nx: Int,
        @Query("ny") ny: Int
    ): Response<Weather>
}
