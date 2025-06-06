package com.example.project_t2.utils

import com.example.project_t2.models.Weather.SentimentEntry
import kotlin.math.pow
import kotlin.math.sqrt

fun euclideanDistance(
    a: SentimentEntry,
    b: SentimentEntry
): Float {
    return sqrt(
        (a.temperature - b.temperature).pow(2) +
        (a.humidity - b.humidity).pow(2) +
        (a.sunshine - b.sunshine).pow(2) +
        (a.wind - b.wind).pow(2) +
        (a.pm10 - b.pm10).pow(2) +
        (a.rainfall - b.rainfall).pow(2)
    )
}