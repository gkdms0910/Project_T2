package com.example.project_t2.utils

import com.example.project_t2.models.Sentiments
import com.example.project_t2.models.Weather.SentimentEntry

fun predictSentimentByWeather(
    input: SentimentEntry,
    dataset: List<SentimentEntry>,
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