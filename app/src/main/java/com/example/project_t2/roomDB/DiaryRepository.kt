package com.example.project_t2.roomDB

class DiaryRepository(private val dao: DiaryDao) {

    suspend fun insertDiary(diary: DiaryEntity) {
        dao.insertDiary(diary)
    }

    suspend fun getDiary(id: Long): DiaryEntity? {
        return dao.getDiary(id)
    }

    suspend fun getAllDiaries(): List<DiaryEntity> {
        return dao.getAllDiary()
    }

    suspend fun searchDiary(keyword: String, sortType: SortType): List<DiaryEntity> {
        return when (sortType) {
            SortType.NEWEST -> dao.searchDiaryNewestFirst(keyword)
            SortType.OLDEST -> dao.searchDiaryOldestFirst(keyword)
        }
    }
}