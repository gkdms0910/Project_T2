package com.example.project_t2.viewmodel

import androidx.compose.material3.ColorScheme
import androidx.lifecycle.ViewModel
import com.example.project_t2.ui.theme.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeViewModel : ViewModel() {

    private val _theme = MutableStateFlow(AppTheme.DEFAULT)
    val theme: StateFlow<AppTheme> = _theme.asStateFlow()

    private val _colorScheme = MutableStateFlow(DefaultColorScheme)
    val colorScheme: StateFlow<ColorScheme> = _colorScheme.asStateFlow()

    fun setTheme(theme: AppTheme) {
        _theme.value = theme
        _colorScheme.value = when (theme) {
            AppTheme.DEFAULT -> DefaultColorScheme
            AppTheme.FOREST -> ForestColorScheme
            AppTheme.SKY -> SkyColorScheme
        }
    }
}