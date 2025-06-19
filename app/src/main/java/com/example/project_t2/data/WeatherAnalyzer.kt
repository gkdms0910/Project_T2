package com.example.project_t2.data

// 데이터 클래스를 수정하여 하늘상태(SKY)와 강수형태(PTY)를 받도록 합니다.
data class WeatherData(val sky: Int?, val pty: Int?)

object WeatherAnalyzer {
    /**
     * 기상청 API 응답값을 바탕으로 날씨를 분석하여 문자열로 반환합니다.
     * @param data 하늘상태(SKY)와 강수형태(PTY) 코드를 담은 WeatherData 객체
     * @return 날씨를 설명하는 문자열 (예: "비", "맑음", "흐림")
     */
    fun analyze(data: WeatherData): String {
        // 먼저 강수형태(PTY)를 확인합니다.
        when (data.pty) {
            1 -> return "비"
            2 -> return "비/눈"
            3 -> return "눈"
            5 -> return "빗방울"
            6 -> return "빗방울/눈날림"
            7 -> return "눈날림"
        }

        // 강수가 없을 경우(PTY == 0), 하늘상태(SKY)로 날씨를 판단합니다.
        when (data.sky) {
            1 -> return "맑음"
            3 -> return "구름많음"
            4 -> return "흐림"
        }

        // 해당하는 코드가 없을 경우 "알 수 없음"을 반환합니다.
        return "알 수 없음"
    }
}