
package com.example.project_t2.graphics

import androidx.annotation.DrawableRes
import com.example.project_t2.R

enum class Emotion(val displayName: String, @DrawableRes val imageResId: Int) {
    HAPPY("행복", R.drawable.happy),
    TENDER("기쁨", R.drawable.tender),
    SAD("보통", R.drawable.sad),
    FEAR("슬픔", R.drawable.fear),
    ANGRY("나쁨", R.drawable.angry)
}