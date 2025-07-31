package com.yusufteker.worthy.screen.dashboard.presentation

import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.MonthlyAmount
import com.yusufteker.worthy.screen.dashboard.domain.EvaluationResult

data class DashboardState(
    val userName: String = "",
    val selectedCurrency: String = "",
    val monthlyWorkHours: Float = 0f,
    val monthlyIncome: Double = 0.0,
    val monthlyExpense: Double = 0.0,
    val desireBudget: Double = 0.0,
    val savingProgress: Float = 0f,   // 0f – 1f
    val isLoading: Boolean = false,
    val fixedExpenseFraction: Float = 0f,
    val desiresSpentFraction: Float = 0f,
    val expensesFraction: Float = 0f,
    var selectedChartIndex: Int? = null,
    var last6MonthAmounts: List<MonthlyAmount> = emptyList(),
    var isBottomSheetOpen: Boolean = false,
    var bottomSheetResult: String? = null,
    var evaluationResult: EvaluationResult? = null,
    var categories: List<Category> = emptyList()

)