package com.example.project_t2.utils

import com.example.project_t2.models.Sentiments
import com.example.project_t2.roomDB.WSentiment.WSentimentEntity
import kotlin.collections.sortedBy

fun predictSentimentByWeather(
    input: WSentimentEntity,
    dataset: List<WSentimentEntity>,
    k: Int = 5
): Sentiments {
    val neighbors = dataset
        .sortedBy { euclideanDistance (it, input) }
        .take(k)
    return neighbors
        .groupingBy { it.sentiment }
        .eachCount()
        .maxByOrNull { it.value }
        ?.key ?: Sentiments.HAPPY // Default Sentiment
}