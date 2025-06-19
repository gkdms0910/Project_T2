package com.example.project_t2.graphics

enum class Emotion(val displayName: String, val emoji: String, val unicode: String) {
    ANGRY("화남", "\uD83D\uDE20", "U+1F620"),     // 😠
    FEAR("두려움", "\uD83D\uDE31", "U+1F631"),     // 😱
    HAPPY("행복", "\uD83D\uDE04", "U+1F604"),     // 😄
    TENDER("평온", "\uD83D\uDE0C", "U+1F60C"),     // 😌
    SAD("슬픔", "\uD83D\uDE22", "U+1F622")        // 😢
}
