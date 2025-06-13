package com.example.project_t2.roomDB
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.time.LocalDateTime

@Dao
interface DiaryDao {
    @Insert
    suspend fun insertDiary(diary: DiaryEntity)

    @Update
    suspend fun updateDiary(diary: DiaryEntity)

    @Query("SELECT * FROM diaryTable WHERE id = :id")
    suspend fun getDiary(id: Int): DiaryEntity?

    @Query("SELECT * FROM diaryTable")
    suspend fun getAllDiary(): List<DiaryEntity>

    @Query("SELECT * FROM diaryTable WHERE time >= :startOfDay AND time < :endOfDay LIMIT 1")
    suspend fun findDiaryForDate(startOfDay: LocalDateTime, endOfDay: LocalDateTime): DiaryEntity?

    @Delete
    suspend fun deleteDiary(diary: DiaryEntity)


}

