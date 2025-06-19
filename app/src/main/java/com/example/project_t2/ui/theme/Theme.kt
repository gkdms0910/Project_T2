package com.example.project_t2.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = SoftPink,
    secondary = SkyBlue,
    tertiary = Peach,
    background = Lavender,
    surface = Peach,
    onPrimary = DarkCharcoal,
    onSecondary = DarkCharcoal,
    onTertiary = DarkCharcoal,
    onBackground = DarkCharcoal,
    onSurface = DarkCharcoal
)

@Composable
fun Project_T2Theme(
    fontSize: AppFontSize = AppFontSize.MEDIUM,
    content: @Composable () -> Unit
) {
    val typography = getTypography(fontSize)
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = typography,
        content = content
    )
}