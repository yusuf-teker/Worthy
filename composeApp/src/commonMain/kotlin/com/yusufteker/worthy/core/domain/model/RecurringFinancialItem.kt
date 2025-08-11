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
    val amount: Money = emptyMoney(),
    val isIncome: Boolean,
    val needType: ExpenseNeedType = ExpenseNeedType.NONE,
    val scheduledDay: Int? = 1,
    val startDate: AppDate,
    val endDate: AppDate? = null,
)


fun RecurringFinancialItem.startDate(): LocalDate =
    LocalDate(year = startDate.year, month = startDate.month, day = 1)

fun RecurringFinancialItem.endDate(): LocalDate? =
    if (endDate?.month != null && endDate.month != null)
        LocalDate(year = endDate.year, month = endDate.month, day = 1)
    else
        null

fun RecurringFinancialItem.isValidFor(year: Int, month: Int): Boolean {
    val start = this.startDate.year * 100 + this.startDate.month

    val end = if (this.endDate?.year != null && this.endDate.month != null) {
        this.endDate.year * 100 + this.endDate.month!!
    } else { //
        currentAppDate().let { it.year * 100 + it.month } // 🔁 Şu anki tarih
    }
    val given = year * 100 + month

    return given in start..end
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
            appDate = AppDate(year = year, month = month),
            amount = validItems.map { it.amount?: emptyMoney() }
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
        startDate  = AppDate(2023, 1),
    ),
    RecurringFinancialItem(
        id = 2,
        groupId = "g2",
        name = "Kira",
        amount = Money(3000.0, Currency.TRY),
        isIncome = false,

        startDate  = AppDate(2023, 1),
    )
)

