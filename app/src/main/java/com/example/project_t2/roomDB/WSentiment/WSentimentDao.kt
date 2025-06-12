package com.example.project_t2.roomDB.WSentiment

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WSentimentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun InsertItem(entity: WSentimentEntity)
    @Update
    suspend fun UpdateItem(entity: WSentimentEntity)
    @Delete
    suspend fun DeleteItem(entity: WSentimentEntity)
    @Query("SELECT * FROM WSentimentTable")
    fun findAll(): Flow<List<WSentimentEntity>>
}