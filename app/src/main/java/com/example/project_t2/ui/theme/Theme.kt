package com.example.project_t2.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ColorScheme // import 추가
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun Project_T2Theme(
    colorScheme: ColorScheme = DefaultColorScheme, // 파라미터 추가 및 기본값 설정
    fontSize: AppFontSize = AppFontSize.MEDIUM,
    content: @Composable () -> Unit
) {
    val typography = getTypography(fontSize)
    MaterialTheme(
        colorScheme = colorScheme, // 파라미터 사용
        typography = typography,
        content = content
    )
}