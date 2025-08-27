package com.yusufteker.worthy.core.domain.model

import com.yusufteker.worthy.core.domain.getCurrentAppDate
import com.yusufteker.worthy.core.domain.service.CurrencyConverter

data class MonthlyAmount(
    val month: String, val amount: Float
)

data class DashboardMonthlyAmount(
    val appDate: AppDate, val amount: List<Money>
)

suspend fun List<DashboardMonthlyAmount>.sumConvertedAmount(
    selectedCurrency: Currency, selectedMonthYear: AppDate, currencyConverter: CurrencyConverter

): Money? {
    val targetMonthlyAmount = this.find { it.appDate == selectedMonthYear }

    val totalMoney = targetMonthlyAmount?.amount?.let {
        currencyConverter.convertAll(it, selectedCurrency)
    }?.sumOf { it ->
        it.amount
    }

    return Money(totalMoney ?: 0.0, selectedCurrency)

}

fun List<DashboardMonthlyAmount>.getCurrent(): DashboardMonthlyAmount? {
    return this.find {
        it.appDate == getCurrentAppDate()
    }
}

fun List<DashboardMonthlyAmount>.getLastMonths(monthCount: Int = 6): List<Int> {
    return this.map {
        it.appDate.month
    }
}


