package com.example.project_t2.roomDB

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class DiaryViewModel(val repository: DiaryRepository) : ViewModel() { // repository를 public으로 변경

    private val _diaryList = MutableStateFlow<List<DiaryEntity>>(emptyList())
    val diaryList: StateFlow<List<DiaryEntity>> = _diaryList.asStateFlow()

    // 검색 결과를 담을 StateFlow 추가
    private val _searchResults = MutableStateFlow<List<DiaryEntity>>(emptyList())
    val searchResults: StateFlow<List<DiaryEntity>> = _searchResults.asStateFlow()

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

    suspend fun getDiaryByDate(date: LocalDate): DiaryEntity? {
        return repository.getDiaryByDate(date)
    }

    fun insertDiary(diary: DiaryEntity) {
        viewModelScope.launch {
            repository.insertDiary(diary)
        }
    }

    fun updateDiary(diary: DiaryEntity) {
        viewModelScope.launch {
            repository.updateDiary(diary)
        }
    }

    // 검색 수행 함수 추가
    fun searchDiaries(keyword: String, sortType: SortType) {
        viewModelScope.launch {
            _diaryList.value = repository.searchDiary(keyword, sortType)
        }
    }

    // 모든 일기를 다시 불러와서 검색 상태를 해제하는 함수
    fun clearSearch() {
        loadAllDiaries()
    }
}