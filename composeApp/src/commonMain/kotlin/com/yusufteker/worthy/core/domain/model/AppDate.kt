package com.yusufteker.worthy.core.domain.model

import com.yusufteker.worthy.core.domain.getCurrentMonth
import com.yusufteker.worthy.core.domain.toEpochMillis
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class AppDate(
    val year: Int, val month: Int, val day: Int? = null
): Comparable<AppDate> {
    fun previousMonth(): AppDate {
        val newMonth = if (month > 1) month - 1 else 12
        val newYear = if (month > 1) year else year - 1
        return copy(year = newYear, month = newMonth)
    }

    fun nextMonth(): AppDate {
        val newMonth = if (month < 12) month + 1 else 1
        val newYear = if (month < 12) year else year + 1
        return copy(year = newYear, month = newMonth)
    }

    override fun toString(): String {
        return if (day != null) "$day/$month/$year" else "$month/$year"
    }

    override fun compareTo(other: AppDate): Int {
        if (year != other.year) return year - other.year
        if (month != other.month) return month - other.month
        return (day ?: 0) - (other.day ?: 0)
    }

}
fun AppDate.getLastMonth(): AppDate {
    if (this.month > 1) {
        return this.copy(month = month - 1)
    } else {
        return this.copy(year = this.year - 1, 12)
    }
}

fun AppDate.toRoomInt(): Int {
    return this.year * 100 + this.month
}

fun AppDate.toEpochMillis(): Long {
    return LocalDate(year, month, day ?: 1).toEpochMillis()
}
fun getLastSixMonths(): List<Int> {
    return (0 until 6).map { offset ->
        val month = (getCurrentMonth() - 5 + offset)
        if (month <= 0) month + 12 else month
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

fun AppDate.format(showDay: Boolean = true): String {
    return "${if (showDay)this.day.toString() + "/" else ""}${this.month}/${this.year}"
}


