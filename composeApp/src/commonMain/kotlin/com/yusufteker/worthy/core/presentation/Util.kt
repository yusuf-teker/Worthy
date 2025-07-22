package com.yusufteker.worthy.core.presentation

import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

fun Double.toRadians(): Double = this * PI / 180


fun Float.formatTwoDecimals(): String {
    val intPart = this.toInt()
    val decimalPart = ((this - intPart) * 100).toInt().absoluteValue
    return "$intPart.${decimalPart.toString().padStart(2, '0')}"
}

fun Double.formatTwoDecimals(): String {
    val intPart = this.toInt()
    val decimalPart = ((this - intPart) * 100).toInt().absoluteValue
    return "$intPart.${decimalPart.toString().padStart(2, '0')}"
}

@OptIn(ExperimentalTime::class)
fun getCurrentYear(): Int {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
}

/**
 * Mevcut ayı Int olarak döndürür (1-12 arası)
 */
@OptIn(ExperimentalTime::class)
fun getCurrentMonth(): Int {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).month.number
}

@OptIn(ExperimentalTime::class)
fun createTimestampId(): String {
    val timestamp = Clock.System.now().toEpochMilliseconds()
    val random = Random.nextInt(1000, 9999)
    return "${timestamp}_${random}"
}
