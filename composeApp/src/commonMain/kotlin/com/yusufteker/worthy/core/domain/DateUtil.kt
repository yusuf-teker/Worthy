package com.yusufteker.worthy.core.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


@OptIn(ExperimentalTime::class)
fun LocalDate.toEpochMillis(timeZone: TimeZone = TimeZone.currentSystemDefault()): Long {
    val localDateTime = this.atStartOfDayIn(timeZone)
    return localDateTime.toEpochMilliseconds()
}

@OptIn(ExperimentalTime::class)
fun Long.toLocalDate(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate {
    return Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone).date
}
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

@OptIn(ExperimentalTime::class)
fun getCurrentLocalDateTime(): kotlinx.datetime.LocalDate {
    return  Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
}

@OptIn(ExperimentalTime::class)
fun getCurrentYear(): Int {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
}
