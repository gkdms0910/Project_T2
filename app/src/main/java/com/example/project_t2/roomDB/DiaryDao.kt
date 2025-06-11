package com.example.project_t2.roomDB
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update // import 추가

@Dao
interface DiaryDao {
    @Insert
    suspend fun insertDiary(diary: DiaryEntity)

    @Update
    suspend fun updateDiary(diary: DiaryEntity) // 수정 함수 추가

    @Query("SELECT * FROM diaryTable WHERE id = :id")
    suspend fun getDiary(id: Int): DiaryEntity? // Long -> Int 타입 변경

    @Query("SELECT * FROM diaryTable")
    suspend fun getAllDiary(): List<DiaryEntity>
}