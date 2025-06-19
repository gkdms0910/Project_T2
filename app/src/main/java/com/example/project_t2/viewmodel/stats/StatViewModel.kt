package com.example.project_t2.viewmodel.stats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.project_t2.data.GetWeather
import com.example.project_t2.graphics.Emotion
import com.example.project_t2.models.Sentiments
import com.example.project_t2.models.Statistics.Period
import com.example.project_t2.roomDB.DiaryRepository
import com.example.project_t2.roomDB.WSentiment.WSentimentEntity
import com.example.project_t2.viewmodel.WSentiment.WSentimentRepository
import com.example.project_t2.utils.getWeatherParser
import com.example.project_t2.utils.predictSentimentByWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatViewModel(
    private val diaryRepository: DiaryRepository,
    private val wSentimentRepository: WSentimentRepository // 예측을 위한 Repository 추가
) : ViewModel() {

    private val _selectedPeriod = MutableStateFlow(Period.WEEK)
    val selectedPeriod: StateFlow<Period> = _selectedPeriod.asStateFlow()

    private val _emotionStats = MutableStateFlow<Map<Emotion, Float>>(emptyMap())
    val emotionStats: StateFlow<Map<Emotion, Float>> = _emotionStats.asStateFlow()

    // 예측 기능을 위한 상태
    private val _wSentimentList = MutableStateFlow<List<WSentimentEntity>>(emptyList())
    private val _predictedSentiment = MutableStateFlow<Sentiments?>(null)
    val predictedSentiment: StateFlow<Sentiments?> = _predictedSentiment.asStateFlow()

    private val _isPredicting = MutableStateFlow(false)
    val isPredicting: StateFlow<Boolean> = _isPredicting.asStateFlow()

    init {
        loadStats()
        // 예측에 사용할 데이터셋 로드
        viewModelScope.launch {
            wSentimentRepository.findAll().collect {
                _wSentimentList.value = it
            }
        }
    }

    fun setPeriod(period: Period) {
        _selectedPeriod.value = period
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            _emotionStats.value = diaryRepository.getEmotionsStats(_selectedPeriod.value)
        }
    }

    fun predictTodaysSentiment() {
        viewModelScope.launch {
            _isPredicting.value = true
            try {
                // 1. 현재 날씨 정보 가져오기
                val currentWeatherDataItems = withContext(Dispatchers.IO) { GetWeather() }
                if (currentWeatherDataItems.isEmpty()) {
                    Log.e("StatViewModel", "Failed to fetch weather data.")
                    _predictedSentiment.value = Sentiments.NONE // 실패 시 '알 수 없음' 상태
                    return@launch
                }

                // 2. 날씨 정보를 예측 모델의 입력 형태로 파싱
                val currentWeatherEntity = getWeatherParser(currentWeatherDataItems)

                // 3. 과거 날씨-감정 데이터셋 준비
                val dataset = _wSentimentList.value
                if (dataset.size < 5) { // k-NN을 위한 최소 데이터 수 확인 (k=5)
                    Log.e("StatViewModel", "Not enough data in dataset to make a prediction.")
                    _predictedSentiment.value = Sentiments.NONE
                    return@launch
                }

                // 4. 감정 예측 실행
                val prediction = predictSentimentByWeather(
                    input = currentWeatherEntity,
                    dataset = dataset
                )
                _predictedSentiment.value = prediction

            } catch (e: Exception) {
                Log.e("StatViewModel", "Error during sentiment prediction", e)
                _predictedSentiment.value = Sentiments.NONE
            } finally {
                _isPredicting.value = false
            }
        }
    }
}

class StatViewModelFactory(
    private val diaryRepository: DiaryRepository,
    private val wSentimentRepository: WSentimentRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatViewModel(diaryRepository, wSentimentRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}