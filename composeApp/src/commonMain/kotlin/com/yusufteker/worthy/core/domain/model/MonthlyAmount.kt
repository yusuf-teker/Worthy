package com.yusufteker.worthy.core.domain.model

import com.yusufteker.worthy.core.domain.service.CurrencyConverter
import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistItem
import worthy.composeapp.generated.resources.Res

data class MonthlyAmount(
    val month: String,
    val amount: Float
)

data class DashboardMonthlyAmount(
    val yearMonth: YearMonth,
    val amount: List<Money>
)

suspend fun List<DashboardMonthlyAmount>.sumConvertedAmount(
    selectedCurrency: Currency,
    selectedMonthYear: YearMonth,
    currencyConverter: CurrencyConverter

): Money? {
    val targetMonthlyAmount = this.find { it.yearMonth == selectedMonthYear }

    val totalMoney = targetMonthlyAmount?.amount?.let {
        currencyConverter.convertAll(it, selectedCurrency)
    }?.sumOf { it ->
        it.amount
    }

    return Money(totalMoney ?: 0.0, selectedCurrency)

}

fun List<DashboardMonthlyAmount>.getCurrent(): DashboardMonthlyAmount?{
    return this.find {
        it.yearMonth == getCurrentYearMonth()
    }
}

fun List<DashboardMonthlyAmount>.getLastMonths(monthCount: Int = 6): List<Int> {
    return this.map {
        it.yearMonth.month
    }
}


