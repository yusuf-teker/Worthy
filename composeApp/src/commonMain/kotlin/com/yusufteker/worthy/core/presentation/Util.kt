package com.yusufteker.worthy.core.presentation

import androidx.compose.runtime.Composable
import com.yusufteker.worthy.core.domain.model.AppDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.month_april
import worthy.composeapp.generated.resources.month_august
import worthy.composeapp.generated.resources.month_december
import worthy.composeapp.generated.resources.month_february
import worthy.composeapp.generated.resources.month_january
import worthy.composeapp.generated.resources.month_july
import worthy.composeapp.generated.resources.month_june
import worthy.composeapp.generated.resources.month_march
import worthy.composeapp.generated.resources.month_may
import worthy.composeapp.generated.resources.month_november
import worthy.composeapp.generated.resources.month_october
import worthy.composeapp.generated.resources.month_september
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

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

fun Int.formatTwoDecimals(): String {
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
fun Long.toFormattedDate(): String {
    val instant = Instant.fromEpochSeconds(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    return "${localDateTime.day.toString().padStart(2, '0')}/" +
            "${localDateTime.month.number.toString().padStart(2, '0')}/" +
            "${localDateTime.year}"
}


@Composable
fun getMonthName(month: Int): UiText {
    return when (month) {
            1 -> UiText.StringResourceId(Res.string.month_january)
            2 -> UiText.StringResourceId(Res.string.month_february)
            3 -> UiText.StringResourceId(Res.string.month_march)
            4 -> UiText.StringResourceId(Res.string.month_april)
            5 ->UiText.StringResourceId( Res.string.month_may)
            6 ->UiText.StringResourceId( Res.string.month_june)
            7 -> UiText.StringResourceId(Res.string.month_july)
            8 ->UiText.StringResourceId( Res.string.month_august)
            9 -> UiText.StringResourceId(Res.string.month_september)
            10 -> UiText.StringResourceId(Res.string.month_october)
            11 -> UiText.StringResourceId(Res.string.month_november)
            12 -> UiText.StringResourceId(Res.string.month_december)
            else -> UiText.StringResourceId(Res.string.month_january) // fallback
        }

}

fun formatPercentageChange(value: Double): String {
    val rounded = (kotlin.math.abs(value) + 0.5).toInt()
    return when {
        value > 0 -> "+ %$rounded"
        value < 0 -> "- %$rounded"
        else -> "%0"
    }
}


@OptIn(ExperimentalTime::class)
fun Long.toAppDate(): AppDate {
    val localDateTime = Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    return AppDate(
        year = localDateTime.year,
        month = localDateTime.month.number,
        day = localDateTime.day
    )
}
