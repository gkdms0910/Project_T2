package com.example.project_t2.data

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DiaryViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiaryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // DiaryRepository를 생성하고, 이를 DiaryViewModel 생성자에 전달합니다.
            return DiaryViewModel(DiaryRepository(application)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}