package com.yusufteker.worthy.feature.settings.presentation

import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Expense
import com.yusufteker.worthy.core.domain.model.Income
import com.yusufteker.worthy.core.domain.model.RecurringFinancialItem

data class SettingsState(
    val incomeRecurringItems: List<RecurringFinancialItem> = emptyList(),
    val uniqueIncomeRecurringItems: List<RecurringFinancialItem> = emptyList(),
    val uniqueExpenseRecurringItems: List<RecurringFinancialItem> = emptyList(),
    val expenseRecurringItems: List<RecurringFinancialItem> = emptyList(),
    val categories: List<Category> = emptyList(),
    val budgetAmount: Float = 0f,
    val savingsAmount: Float = 0f,
    val totalFixedIncome: Float = 0f,
    val totalFixedExpenses: Float = 0f,
    val weeklyWorkHours: Int = 40,
    val selectedCurrency: String = "USD",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
    )
