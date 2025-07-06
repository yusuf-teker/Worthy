package com.yusufteker.worthy.feature.dashboard.presentation

import com.yusufteker.worthy.core.domain.MonthlyAmount

data class DashboardState(
    val userName: String = "",
    val monthlyIncome: Double = 0.0,
    val monthlyExpense: Double = 0.0,
    val savingProgress: Float = 0f,   // 0f – 1f
    val isLoading: Boolean = false,
    val fixedExpenseFraction: Float = 0f,
    val desiresSpentFraction: Float = 0f,
    val expensesFraction: Float = 0f,
    var selectedChartIndex: Int? = null,
    var last6MonthAmounts: List<MonthlyAmount> = emptyList()
)