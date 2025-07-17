package com.yusufteker.worthy.feature.settings.presentation

import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Expense
import com.yusufteker.worthy.core.domain.model.Income

data class SettingsState(
    val incomeItems: List<Income> = emptyList(),
    val expenseItems: List<Expense> = emptyList(),
    val categories: List<Category> = emptyList(),             // İstersen ekle
    val budgetAmount: Float = 0f,
    val weeklyWorkHours: Int = 40,
    val selectedCurrency: String = "USD",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
    )
