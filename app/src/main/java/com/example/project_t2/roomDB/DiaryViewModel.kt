package com.example.project_t2.roomDB

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DiaryViewModel(private val repository: DiaryRepository) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<DiaryEntity>>(emptyList())
    val searchResults: StateFlow<List<DiaryEntity>> = _searchResults

    fun searchDiary(keyword: String, sortType: SortType) {
        viewModelScope.launch {
            val results = repository.searchDiary(keyword, sortType)
            _searchResults.value = results
        }
    }
}