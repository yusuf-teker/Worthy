package com.yusufteker.worthy.core.domain.model

import com.yusufteker.worthy.core.data.database.entities.ExpenseNeedType
import kotlinx.datetime.LocalDate

data class RecurringFinancialItem(
    val id: Int = 0,
    val groupId: String,
    val name: String,
    val amount: Double,
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
