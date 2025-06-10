package com.example.Project_T2.data
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    // 리포지토리 선언 (RetrofitInstance를 통해 API 사용)
    private val repository = WeatherRepository()

    // 외부에서 접근할 수 있는 상태 (읽기 전용)
    private val _weatherData = MutableStateFlow<Weather?>(null)
    val weatherData: StateFlow<Weather?> = _weatherData

    // API를 호출해서 데이터를 받아오는 함수
    fun loadWeather(
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int
    ) {
        viewModelScope.launch {
            val result = repository.getWeather(
                baseDate = baseDate,
                baseTime = baseTime,
                nx = nx,
                ny = ny
            )
            _weatherData.value = result
        }
    }
}