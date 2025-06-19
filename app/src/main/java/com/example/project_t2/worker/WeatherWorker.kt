package com.example.project_t2.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.project_t2.data.GetWeather
import com.example.project_t2.roomDB.WSentiment.WSentimentDatabase
import com.example.project_t2.viewmodel.WSentiment.WSentimentRepository
import com.example.project_t2.models.Sentiments
import com.example.project_t2.models.Weathers
import com.example.project_t2.roomDB.WSentiment.WSentimentEntity
import com.example.project_t2.utils.getWeatherParser
import com.example.project_t2.utils.predictSentimentByWeather
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import java.time.LocalTime

class WeatherWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val item: WSentimentEntity = try {
            getWeatherParser(GetWeather())
        } catch (e: Exception) {
            return Result.failure()
        }

        // DB 및 레포지토리 준비
        val db = WSentimentDatabase.getDBInstance(applicationContext)
        val repo = WSentimentRepository(db)

        // WSentimentEntity 생성
        val entity = WSentimentEntity(
            temperature = item.temperature,
            humidity = item.humidity,
            wind = item.wind,
            rainfall = item.rainfall,
            sentiment = item.sentiment,
            date = LocalDate.now().toString(),
            time = LocalTime.now().hour,
            hours = 1
        )

        // 업데이트
        repo.updateItem(entity)

        return Result.success()
    }
}