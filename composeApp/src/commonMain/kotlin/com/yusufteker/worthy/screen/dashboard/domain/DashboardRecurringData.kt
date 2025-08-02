package com.yusufteker.worthy.screen.dashboard.domain

import com.yusufteker.worthy.core.domain.model.DashboardMonthlyAmount

data class DashboardRecurringData(
    val incomes: List<DashboardMonthlyAmount>,
    val expenses: List<DashboardMonthlyAmount>
)
