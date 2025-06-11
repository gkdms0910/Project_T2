package com.example.project_t2.data

import androidx.lifecycle.ViewModel // ViewModel로 변경
import androidx.lifecycle.viewModelScope
import com.example.project_t2.roomDB.DiaryEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// AndroidViewModel -> ViewModel로 변경
// 생성자에서 repository를 직접 받습니다.
class DiaryViewModel(private val repository: DiaryRepository) : ViewModel() {

    private val _selectedDiary = MutableStateFlow<DiaryEntity?>(null)
    val selectedDiary: StateFlow<DiaryEntity?> = _selectedDiary

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