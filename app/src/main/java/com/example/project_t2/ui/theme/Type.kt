package com.example.project_t2.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

enum class AppFontSize(val scale: Float) {
    SMALL(0.8f),
    MEDIUM(1.0f),
    LARGE(1.2f)
}

fun getTypography(fontSize: AppFontSize): Typography {
    val scale = fontSize.scale
    return Typography(
        bodyLarge = TextStyle(
            fontFamily = MainFont, // Custom Font
            fontWeight = FontWeight.Normal,
            fontSize = (16 * scale).sp,
            lineHeight = (24 * scale).sp,
            letterSpacing = (0.5 * scale).sp
        ),
        titleLarge = TextStyle(
            fontFamily = MainFont, // Custom Font
            fontWeight = FontWeight.Bold,
            fontSize = (22 * scale).sp,
            lineHeight = (28 * scale).sp,
            letterSpacing = 0.sp
        ),
        labelSmall = TextStyle(
            fontFamily = MainFont, // Custom Font
            fontWeight = FontWeight.Medium,
            fontSize = (11 * scale).sp,
            lineHeight = (16 * scale).sp,
            letterSpacing = (0.5 * scale).sp
        )
    )
}

val Typography = getTypography(AppFontSize.MEDIUM)