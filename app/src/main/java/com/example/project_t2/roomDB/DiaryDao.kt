package com.example.project_t2.roomDB
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DiaryDao {
    @Insert
    suspend fun insertDiary(diary: DiaryEntity)

    //앱 기능 상 삭제 기능은 없음

    @Query("SELECT * FROM diaryTable WHERE id = :id")
    suspend fun getDiary(id: Long): DiaryEntity?

    @Query("SELECT * FROM diaryTable")
    suspend fun getAllDiary(): List<DiaryEntity>

    @Query("SELECT * FROM diaryTable WHERE title LIKE '%' || :keyword || '%' OR content LIKE '%' || :keyword || '%' ORDER BY time DESC")
    suspend fun searchDiaryNewestFirst(keyword: String): List<DiaryEntity>

    @Query("SELECT * FROM diaryTable WHERE title LIKE '%' || :keyword || '%' OR content LIKE '%' || :keyword || '%' ORDER BY time ASC")
    suspend fun searchDiaryOldestFirst(keyword: String): List<DiaryEntity>
    //fun getAllDiary():Flow<List<DiaryEntity>>
}