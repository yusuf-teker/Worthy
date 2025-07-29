package com.yusufteker.worthy.core.presentation

import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.pow
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

fun Float.toFormattedWithThousandsSeparator(
    digits: Int = 2,
    separator: Char = '.'
): String {
    val multiplier = 10.0.pow(digits).toFloat()
    val rounded = kotlin.math.round(this * multiplier) / multiplier
    val parts = rounded.toString().split(".")

    val integerPart = parts[0]
    val decimalPart = parts.getOrNull(1) ?: ""

    val formattedInt = integerPart.reversed()
        .chunked(3)
        .joinToString(separator.toString())
        .reversed()

    val formattedDecimal = decimalPart.padEnd(digits, '0')

    return "$formattedInt.$formattedDecimal"
}

fun Double.toFormattedWithThousandsSeparator(
    separator: Char = '.'
): String {
    val digits = if (this < 1_000_000) 2 else 0

    val multiplier = 10.0.pow(digits)
    val rounded = kotlin.math.round(this * multiplier) / multiplier
    val parts = rounded.toString().split(".")

    val integerPart = parts[0]
    val decimalPart = parts.getOrNull(1) ?: ""

    val formattedInt = integerPart.reversed()
        .chunked(3)
        .joinToString(separator.toString())
        .reversed()

    val formattedDecimal = if (digits > 0)
        decimalPart.padEnd(digits, '0')
    else
        ""

    return if (digits > 0) "$formattedInt.$formattedDecimal" else formattedInt
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
