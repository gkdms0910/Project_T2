package com.example.project_t2.roomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DiaryDao {
    @Insert
    suspend fun insertDiary(diary: DiaryEntity)

    @Insert
    suspend fun insertAll(diaries: List<DiaryEntity>)

    @Update
    suspend fun updateDiary(diary: DiaryEntity)

    @Query("SELECT * FROM diaryTable WHERE id = :id")
    suspend fun getDiary(id: Long): DiaryEntity?

    @Query("SELECT * FROM diaryTable")
    suspend fun getAllDiary(): List<DiaryEntity>

    @Query("SELECT * FROM diaryTable WHERE title LIKE '%' || :keyword || '%' OR content LIKE '%' || :keyword || '%' ORDER BY time DESC")
    suspend fun searchDiaryNewestFirst(keyword: String): List<DiaryEntity>

    @Query("SELECT * FROM diaryTable WHERE title LIKE '%' || :keyword || '%' OR content LIKE '%' || :keyword || '%' ORDER BY time ASC")
    suspend fun searchDiaryOldestFirst(keyword: String): List<DiaryEntity>

    @Query("SELECT * FROM diaryTable WHERE time >= :from")
    suspend fun getDiaryFromDate(from: String): List<DiaryEntity>

    @Query("SELECT * FROM diaryTable WHERE date(time) = :date")
    suspend fun getDiaryByDate(date: String): DiaryEntity?
}