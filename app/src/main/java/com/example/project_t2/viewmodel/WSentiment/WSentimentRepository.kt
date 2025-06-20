package com.example.project_t2.viewmodel.WSentiment

import com.example.project_t2.models.Sentiments
import com.example.project_t2.roomDB.WSentiment.WSentimentDatabase
import com.example.project_t2.roomDB.WSentiment.WSentimentEntity
import kotlinx.coroutines.flow.firstOrNull

class WSentimentRepository(private val db: WSentimentDatabase) {
    val dao = db.getWSentimentDao()

    suspend fun insertItem(item: WSentimentEntity) {
        dao.InsertItem(item)
    }

    suspend fun deleteItem(item: WSentimentEntity) {
        dao.DeleteItem(item)
    }

    suspend fun updateItem(item: WSentimentEntity) {
        val lastItem = dao.findAll().firstOrNull()?.maxByOrNull { it.id } ?: null

        if (lastItem == null || lastItem.date != item.date) {
            dao.InsertItem(item)
            return
        }

        if (lastItem.time != item.time || lastItem.sentiment != Sentiments.NONE) {
            return
        }

        val updateItem = item.copy(
            temperature = lastItem.temperature + item.temperature,
            humidity = lastItem.humidity + item.humidity,
            wind = lastItem.wind + item.wind,
            rainfall = lastItem.rainfall + item.rainfall,
            sentiment = item.sentiment,
            hours = lastItem.hours + 1,
            date = item.date
        )
        dao.UpdateItem(updateItem)
    }

    fun findAll() = dao.findAll()
}