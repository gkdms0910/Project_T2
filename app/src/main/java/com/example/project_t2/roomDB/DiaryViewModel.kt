package com.example.project_t2.roomDB

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class DiaryViewModel(private val repository: DiaryRepository) : ViewModel() {

    private val _diaryList = MutableStateFlow<List<DiaryEntity>>(emptyList())
    val diaryList: StateFlow<List<DiaryEntity>> = _diaryList.asStateFlow()

    init {
        loadAllDiaries()
    }

    fun loadAllDiaries() {
        viewModelScope.launch {
            _diaryList.value = repository.getAllDiaries()
        }
    }
    suspend fun getDiary(id: Long): DiaryEntity? {
        return repository.getDiary(id)
    }

    // 날짜로 일기 조회
    suspend fun getDiaryByDate(date: LocalDate): DiaryEntity? {
        return repository.getDiaryByDate(date)
    }

    // 일기 추가
    fun insertDiary(diary: DiaryEntity) {
        viewModelScope.launch {
            repository.insertDiary(diary)
        }
    }

    // 일기 수정
    fun updateDiary(diary: DiaryEntity) {
        viewModelScope.launch {
            repository.updateDiary(diary)
        }
    }
}