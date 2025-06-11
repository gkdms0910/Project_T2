package com.example.project_t2.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_t2.roomDB.DiaryEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DiaryViewModel(private val repository: DiaryRepository) : ViewModel() {

    private val _selectedDiary = MutableStateFlow<DiaryEntity?>(null)
    val selectedDiary: StateFlow<DiaryEntity?> = _selectedDiary

    private val _diaryList = MutableStateFlow<List<DiaryEntity>>(emptyList())
    val diaryList: StateFlow<List<DiaryEntity>> = _diaryList

    fun getAllDiaries() {
        viewModelScope.launch {
            _diaryList.value = repository.getAllDiaries()
        }
    }
    fun getDiary(id: Int) {
        viewModelScope.launch {
            _selectedDiary.value = repository.getDiary(id)
        }
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
}