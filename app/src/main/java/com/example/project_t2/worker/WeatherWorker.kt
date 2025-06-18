package com.example.project_t2.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.project_t2.roomDB.WSentiment.WSentimentDatabase
import com.example.project_t2.viewmodel.WSentiment.WSentimentRepository
import com.example.project_t2.models.Sentiments
import com.example.project_t2.models.Weathers
import com.example.project_t2.roomDB.WSentiment.WSentimentEntity
import com.example.project_t2.utils.predictSentimentByWeather
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import java.time.LocalTime

class WeatherWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val item: WSentimentEntity = try {
            // TODO: 날씨 정보 가져오기 (현재는 임시 값 사용, 실제 API 호출 필요)
            WSentimentEntity(
                temperature = 0f,
                humidity = 0f,
                sunshine = 0f,
                wind = 0f,
                pm10 = 0f,
                rainfall = 0f,
                sentiment = Sentiments.NONE,
                date = LocalDate.now().toString(),
                time = LocalTime.now().hour,
                hours = 1,
                weather = Weathers.SUNNY
            )
        } catch (e: Exception) {
            // 예외 처리
            return Result.failure()
        }

        // DB 및 레포지토리 준비
        val db = WSentimentDatabase.getDBInstance(applicationContext)
        val repo = WSentimentRepository(db)

        // WSentimentEntity 생성
        val entity = WSentimentEntity(
            temperature = item.temperature,
            humidity = item.humidity,
            sunshine = item.sunshine,
            wind = item.wind,
            pm10 = item.pm10,
            rainfall = item.rainfall,
            sentiment = item.sentiment,
            date = LocalDate.now().toString(),
            time = LocalTime.now().hour,
            weather = item.weather
        )

        // 업데이트
        repo.updateItem(entity)

        return Result.success()
    }
}