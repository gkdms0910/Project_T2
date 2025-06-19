package com.example.project_t2.graphics

enum class Emotion(val displayName: String, val emoji: String, val unicode: String) {
    SMILE("미소", "🙂", "U+1F642"),
    ANGRY("화남", "😠", "U+1F620"),
    FEAR("두려움", "😱", "U+1F631"),
    HAPPY("행복", "😄", "U+1F604"),
    TENDER("평온", "😌", "U+1F60C"),
    SAD("슬픔", "😢", "U+1F622"),
    BORED("지루함", "😑", "U+1F611")
}
