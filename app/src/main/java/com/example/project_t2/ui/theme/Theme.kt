package com.example.project_t2.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = LightBrown,
    secondary = SoftBlue,
    tertiary = Cream,
    background = Cream,
    surface = Cream,
    onPrimary = Color.White,
    onSecondary = DarkCharcoal,
    onTertiary = DarkCharcoal,
    onBackground = DarkCharcoal,
    onSurface = DarkCharcoal
)

@Composable
fun Project_T2Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}