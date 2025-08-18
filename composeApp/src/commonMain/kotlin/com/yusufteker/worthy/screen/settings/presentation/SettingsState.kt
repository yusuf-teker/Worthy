package com.yusufteker.worthy.screen.settings.presentation

import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.RecurringFinancialItem
import com.yusufteker.worthy.core.domain.model.emptyMoney
import com.yusufteker.worthy.core.presentation.base.BaseState

data class SettingsState(
    val incomeRecurringItems: List<RecurringFinancialItem> = emptyList(),
    val uniqueIncomeRecurringItems: List<RecurringFinancialItem> = emptyList(),
    val uniqueExpenseRecurringItems: List<RecurringFinancialItem> = emptyList(),
    val expenseRecurringItems: List<RecurringFinancialItem> = emptyList(),
    val categories: List<Category> = emptyList(),
    val budgetAmount: Money = emptyMoney(),
    val convertedBudgetAmount: Double = 0.0,
    val savingsAmount: Double = 0.0,
    val totalFixedIncome: Double = 0.0,
    val totalFixedExpenses: Double = 0.0,
    val weeklyWorkHours: Int = 40,
    val selectedCurrency: Currency = Currency.TRY,
    override val isLoading: Boolean = false,
    val errorMessage: String? = null
    ): BaseState{
        override fun copyWithLoading(isLoading: Boolean) = copy(isLoading = isLoading)

    }
