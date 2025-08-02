package com.yusufteker.worthy.core.domain.model

import com.yusufteker.worthy.core.data.database.entities.ExpenseNeedType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class RecurringFinancialItem(
    val id: Int = 0,
    val groupId: String,
    val name: String,
    val amount: Money,
    val isIncome: Boolean,
    val needType: ExpenseNeedType = ExpenseNeedType.NONE,
    val scheduledDay: Int? = 1,
    val startMonth: Int,
    val startYear: Int,
    val endMonth: Int? = null,
    val endYear: Int? = null,
)


fun RecurringFinancialItem.startDate(): LocalDate =
    LocalDate(year = startYear, month = startMonth, day = 1)

fun RecurringFinancialItem.endDate(): LocalDate? =
    if (endMonth != null && endYear != null)
        LocalDate(year = endYear, month = endMonth, day = 1)
    else
        null

fun RecurringFinancialItem.isValidFor(year: Int, month: Int): Boolean {
    val start = year * 100 + startMonth
    val end = if (endYear != null && endMonth != null) endYear!! * 100 + endMonth!! else Int.MAX_VALUE
    val current = year * 100 + month

    return current in start..end
}

fun getRecentMonths(currentDate: LocalDate, count: Int): List<Pair<Int, Int>> {
    val months = mutableListOf<Pair<Int, Int>>()
    var year = currentDate.year
    var month = currentDate.month.number

    repeat(count) {
        months.add(Pair(year, month))
        month -= 1
        if (month == 0) {
            month = 12
            year -= 1
        }
    }

    return months.reversed() // eskiden yeniye doğru
}

@OptIn(ExperimentalTime::class)
fun generateMonthlyAmounts(
    isIncome: Boolean? = null,
    items: List<RecurringFinancialItem>,
    referenceDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    monthCount: Int
): List<DashboardMonthlyAmount> {
    val months = getRecentMonths(referenceDate, monthCount)

    return months.map { (year, month) ->
        val validItems = items.filter { it.isValidFor(year, month)  && (if (isIncome != null) it.isIncome == isIncome  else true)}
        DashboardMonthlyAmount(
            yearMonth = YearMonth(year = year, month = month),
            amount = validItems.map { it.amount }
        )
    }
}

val items = listOf(
    RecurringFinancialItem(
        id = 1,
        groupId = "g1",
        name = "Maaş",
        amount = Money(10000.0, Currency.TRY    ),
        isIncome = true,
        startMonth = 1,
        startYear = 2024
    ),
    RecurringFinancialItem(
        id = 2,
        groupId = "g2",
        name = "Kira",
        amount = Money(3000.0, Currency.TRY),
        isIncome = false,
        startMonth = 2,
        startYear = 2025,
        endMonth = 4,
        endYear = 2025
    )
)

val result = generateMonthlyAmounts(
    isIncome = true,
    items = items,
    referenceDate = LocalDate(2025, 4, 18),
    monthCount = 3
)

