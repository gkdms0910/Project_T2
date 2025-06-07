package com.example.project_t2.graphics

enum class Emotion(val displayName: String, val emoji: String, val unicode: String) {
    SMILE("미소", "\uD83D\uDE0A", "U+1F60A"),   // 😊
    BORED("지루함", "\uD83D\uDE12", "U+1F612"), // 😒
    SAD("슬픔", "\uD83D\uDE22", "U+1F622"),    // 😢
    ANGRY("화남", "\uD83D\uDE20", "U+1F620")   // 😠
}