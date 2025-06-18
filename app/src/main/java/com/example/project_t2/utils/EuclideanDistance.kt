package com.example.project_t2.utils

import com.example.project_t2.roomDB.WSentiment.WSentimentEntity
import kotlin.math.pow
import kotlin.math.sqrt

fun euclideanDistance(
    a: WSentimentEntity,
    b: WSentimentEntity
): Float {
    return sqrt(
        (a.temperature / a.hours - b.temperature / b.hours).pow(2) +
        (a.humidity / a.hours - b.humidity / b.hours).pow(2) +
        (a.sunshine / a.hours - b.sunshine / b.hours).pow(2) +
        (a.wind / a.hours - b.wind / b.hours).pow(2) +
        (a.pm10 / a.hours - b.pm10 / b.hours).pow(2) +
        (a.rainfall / a.hours - b.rainfall / b.hours).pow(2)
    )
}