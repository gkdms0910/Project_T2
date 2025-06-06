package com.example.project_t2.models.KoBERT

import kotlinx.serialization.Serializable

/**
 * KoBERTRequestBody는 KoBERT 모델을 사용하여 감정 분석을 수행할 때 필요한 요청 본문을 나타냅니다.
 *
 * @property text KoBERT 모델을 사용하여 감정 분석을 수행할 텍스트.
 */
@Serializable
data class KoBERTRequestBody(
    val text: String
)
