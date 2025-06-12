package com.example.project_t2.viewmodel

import com.example.project_t2.roomDB.WSentiment.WSentimentDatabase
import com.example.project_t2.roomDB.WSentiment.WSentimentEntity

class WSentimentRepository(private val db: WSentimentDatabase) {
    val dao = db.getWSentimentDao()

    suspend fun insertItem(item: WSentimentEntity) {
        dao.InsertItem(item)
    }

    suspend fun updateItem(item: WSentimentEntity) {
        dao.UpdateItem(item)
    }

    suspend fun deleteItem(item: WSentimentEntity) {
        dao.DeleteItem(item)
    }

    fun findAll() = dao.findAll()
}