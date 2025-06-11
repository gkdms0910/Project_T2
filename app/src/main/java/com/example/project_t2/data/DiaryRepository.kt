package com.example.project_t2.data

import android.content.Context
import com.example.project_t2.roomDB.DiaryDatabase
import com.example.project_t2.roomDB.DiaryEntity

class DiaryRepository(context: Context) {
    private val db = DiaryDatabase.getDBInstance(context)
    private val diaryDao = db.getDiaryDao()

    suspend fun getDiary(id: Int) = diaryDao.getDiary(id)
    suspend fun insertDiary(diary: DiaryEntity) = diaryDao.insertDiary(diary)
    suspend fun updateDiary(diary: DiaryEntity) = diaryDao.updateDiary(diary)
    suspend fun getAllDiaries(): List<DiaryEntity> = diaryDao.getAllDiary()
}