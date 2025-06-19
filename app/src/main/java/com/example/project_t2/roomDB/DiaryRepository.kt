package com.example.project_t2.roomDB

import com.example.project_t2.graphics.Emotion
import com.example.project_t2.models.Statistics.Period
import java.time.LocalDate
import java.time.LocalDateTime

class DiaryRepository(private val dao: DiaryDao) {

    suspend fun insertDiary(diary: DiaryEntity) {
        dao.insertDiary(diary)
    }

    suspend fun updateDiary(diary: DiaryEntity) { // 수정 함수 추가
        dao.updateDiary(diary)
    }

    suspend fun getDiaryByDate(date: LocalDate): DiaryEntity? { // 날짜로 조회하는 함수 추가
        return dao.getDiaryByDate(date.toString())
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

    // 7일 / 30일 / 6개월 / 1년 / 전체 기간 동안의 감정 통계
    suspend fun getEmotionsStats(period: Period): Map<Emotion, Float> {
        val from = when (period) {
            Period.WEEK -> LocalDateTime.now().minusWeeks(1)
            Period.MONTH -> LocalDateTime.now().minusMonths(1)
            Period.HALF_YEAR -> LocalDateTime.now().minusMonths(6)
            Period.YEAR -> LocalDateTime.now().minusYears(1)
            Period.ALL -> LocalDateTime.MIN // 모든 기간을 포함
        }
        val diaries = dao.getDiaryFromDate(from.toString())
        val total = diaries.size.toFloat()
        return diaries.groupingBy { it.emotion }
            .eachCount()
            .mapValues { it.value / total }
    }
}