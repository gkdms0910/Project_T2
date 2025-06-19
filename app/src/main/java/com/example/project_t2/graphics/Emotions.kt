
package com.example.project_t2.graphics

import androidx.annotation.DrawableRes
import com.example.project_t2.R

enum class Emotion(val displayName: String, @DrawableRes val imageResId: Int) {
    HAPPY("행복", R.drawable.happy), // R.drawable.happy는 실제 추가한 이미지 파일 이름입니다.
    JOY("기쁨", R.drawable.joy),
    TENDER("보통", R.drawable.tender),
    SAD("슬픔", R.drawable.sad),
    BAD("나쁨", R.drawable.bad)
}