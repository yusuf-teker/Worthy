package com.yusufteker.worthy.screen.dashboard.presentation

import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.YearMonth

sealed interface DashboardAction {
    object Refresh : DashboardAction
    object EvaluateButtonClicked : DashboardAction
    data class ChartSelected(val index: Int) : DashboardAction

    object CloseBottomSheetClicked: DashboardAction

    data class CalculateButtonClicked(val money: Money): DashboardAction

    data class OnSelectedMonthChanged(val yearMonth: YearMonth) : DashboardAction

}