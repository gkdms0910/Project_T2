package com.example.project_t2.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// --- 기본 테마 (기존 코드) ---
val SoftPink = Color(0xFFFAD2E1)
val Peach = Color(0xFFFDE8E1)
val SkyBlue = Color(0xFFB0E0E6)
val Lavender = Color(0xFFE6E6FA)
val DarkCharcoal = Color(0xFF333333)

val DefaultColorScheme = lightColorScheme(
    primary = SoftPink,
    secondary = SkyBlue,
    tertiary = Peach,
    background = Lavender,
    surface = Peach,
    onPrimary = DarkCharcoal,
    onSecondary = DarkCharcoal,
    onTertiary = DarkCharcoal,
    onBackground = DarkCharcoal,
    onSurface = DarkCharcoal,
    // 아래 2줄 추가
    error = Color(0xFFB71C1C),
    surfaceVariant = Color(0xFFD7CCC8)
)

// --- 숲 테마 (새로 추가) ---
val ForestGreen = Color(0xFFA5C2A3)
val ForestBrown = Color(0xFFD3C3B1)
val LightGreen = Color(0xFFE3E8E1)
val LightBrown = Color(0xFFF0EBE5)

val ForestColorScheme = lightColorScheme(
    primary = ForestGreen,
    secondary = ForestBrown,
    tertiary = LightGreen,
    background = LightBrown,
    surface = LightGreen,
    onPrimary = DarkCharcoal,
    onSecondary = DarkCharcoal,
    onTertiary = DarkCharcoal,
    onBackground = DarkCharcoal,
    onSurface = DarkCharcoal,
    // 아래 2줄 추가
    error = Color(0xFFD84315),
    surfaceVariant = Color(0xFFC5E1A5)
)

// --- 하늘 테마 (새로 추가) ---
val DeepSkyBlue = Color(0xFF87CEEB)
val LightCloud = Color(0xFFF5F5F5)
val PaleBlue = Color(0xFFE6F4F1)

val SkyColorScheme = lightColorScheme(
    primary = DeepSkyBlue,
    secondary = SkyBlue,
    tertiary = PaleBlue,
    background = LightCloud,
    surface = PaleBlue,
    onPrimary = DarkCharcoal,
    onSecondary = DarkCharcoal,
    onTertiary = DarkCharcoal,
    onBackground = DarkCharcoal,
    onSurface = DarkCharcoal,
    // 아래 2줄 추가
    error = Color(0xFFE57373),
    surfaceVariant = Color(0xFFB0BEC5)
)