package com.example.project_t2.models

import kotlinx.serialization.Serializable

/**
 * KoBERTResponse는 KoBERT 모델을 사용하여 감정 분석을 수행한 결과를 나타냅니다.
 *
 * @property text 분석된 텍스트.
 * @property predicted_label_id 예측된 감정 레이블의 ID.
 * @property predicted_label 예측된 감정 레이블의 이름.
 * @property probabilities 각 감정 레이블에 대한 확률을 나타내는 맵.
 */
@Serializable
data class KoBERTResponse(
    val text: String,
    val predicted_label_id: Int,
    val predicted_label: String,
    val probabilities: Map<String, Double>
)
