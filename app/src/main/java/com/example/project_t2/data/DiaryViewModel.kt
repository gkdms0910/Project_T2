package com.example.project_t2.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_t2.api.getKoBERTResponse
import com.example.project_t2.graphics.Emotion
import com.example.project_t2.roomDB.DiaryEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class DiaryViewModel(private val repository: DiaryRepository) : ViewModel() {

    private val _selectedDiary = MutableStateFlow<DiaryEntity?>(null)
    val selectedDiary: StateFlow<DiaryEntity?> = _selectedDiary

    private val _diaryList = MutableStateFlow<List<DiaryEntity>>(emptyList())
    val diaryList: StateFlow<List<DiaryEntity>> = _diaryList

    private val _navigateToDiary = MutableStateFlow<Int?>(null)
    val navigateToDiary: StateFlow<Int?> = _navigateToDiary

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _analysisResult = MutableStateFlow<Emotion?>(null)
    val analysisResult: StateFlow<Emotion?> = _analysisResult

    private fun mapLabelToEmotion(label: String): Emotion {
        return when (label) {
            "행복" -> Emotion.SMILE
            "슬픔" -> Emotion.SAD
            "분노" -> Emotion.ANGRY
            else -> Emotion.BORED
        }
    }

    fun analyzeEmotion(content: String) {
        if (content.isBlank()) {
            _analysisResult.value = Emotion.SMILE
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = getKoBERTResponse(content)
                _analysisResult.value = mapLabelToEmotion(response.predicted_label)
            } catch (e: Exception) {
                _analysisResult.value = Emotion.SMILE
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onAnalysisComplete() {
        _analysisResult.value = null
    }

    fun findDiaryForToday() {
        viewModelScope.launch {
            val todayDiary = repository.findDiaryForDate(LocalDate.now())
            _navigateToDiary.value = todayDiary?.id ?: -1
        }
    }

    fun onNavigationComplete() {
        _navigateToDiary.value = null
    }

    fun deleteDiary(diary: DiaryEntity) {
        viewModelScope.launch {
            repository.deleteDiary(diary)
            getAllDiaries()
        }
    }

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