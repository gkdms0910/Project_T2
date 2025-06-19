// BackgroundViewModel.kt
package com.example.project_t2.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.project_t2.R

class BackgroundViewModel : ViewModel() {
    private val _backgroundResId = MutableStateFlow(DEFAULT_BACKGROUND)
    val backgroundResId: StateFlow<Int> = _backgroundResId

    fun setBackground(resId: Int) {
        _backgroundResId.value = resId
    }

    companion object {
        val DEFAULT_BACKGROUND = R.drawable.paper_texture
        val ALT_BACKGROUND = R.drawable.white
    }
}
