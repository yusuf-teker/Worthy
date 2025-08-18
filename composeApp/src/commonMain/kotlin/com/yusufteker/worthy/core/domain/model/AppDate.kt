package com.yusufteker.worthy.core.domain.model

import com.yusufteker.worthy.core.domain.getCurrentMonth
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class AppDate(
    val year: Int, val month: Int, val day: Int? = null
) {
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
}

@OptIn(ExperimentalTime::class)
fun currentAppDate(includeDay: Boolean = false): AppDate {
    val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
    return AppDate(
        year = today.year, month = today.month.number, day = if (includeDay) today.day else null
    )
}

fun AppDate.getLastMonth(): AppDate {
    if (this.month > 1) {
        return this.copy(month = month - 1)
    } else {
        return this.copy(year = this.year - 1, 12)
    }

}

fun getLastSixMonths(): List<Int> {
    return (0 until 6).map { offset ->
        val month = (getCurrentMonth() - 5 + offset)
        if (month <= 0) month + 12 else month
    }
}