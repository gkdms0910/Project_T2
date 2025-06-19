package com.example.project_t2.data

import android.content.Context
import com.example.project_t2.roomDB.DiaryDatabase
import com.example.project_t2.roomDB.DiaryEntity
import java.time.LocalDate

class DiaryRepository(context: Context) {
    private val db = DiaryDatabase.getDBInstance(context)
    private val diaryDao = db.getDiaryDao()

    suspend fun getDiary(id: Int) = diaryDao.getDiary(id)
    suspend fun insertDiary(diary: DiaryEntity) = diaryDao.insertDiary(diary)
    suspend fun updateDiary(diary: DiaryEntity) = diaryDao.updateDiary(diary)
    suspend fun getAllDiaries(): List<DiaryEntity> = diaryDao.getAllDiary()
    suspend fun findDiaryForDate(date: LocalDate): DiaryEntity? {
        val startOfDay = date.atStartOfDay()
        val endOfDay = date.plusDays(1).atStartOfDay()
        return diaryDao.findDiaryForDate(startOfDay, endOfDay)
    }

    suspend fun deleteDiary(diary: DiaryEntity) = diaryDao.deleteDiary(diary)

    
}