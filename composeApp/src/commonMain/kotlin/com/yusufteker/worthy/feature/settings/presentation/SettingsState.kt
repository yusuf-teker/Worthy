package com.yusufteker.worthy.feature.settings.presentation

import com.yusufteker.worthy.feature.settings.domain.ExpenseItem
import com.yusufteker.worthy.feature.settings.domain.IncomeItem

data class SettingsState(
    val incomeItems: List<IncomeItem> = listOf(),
    val fixedExpenseItems: List<ExpenseItem> = listOf(),
    val budgetAmount: Float = 0f,
)
