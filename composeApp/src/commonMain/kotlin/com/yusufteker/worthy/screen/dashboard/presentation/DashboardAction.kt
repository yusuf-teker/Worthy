package com.yusufteker.worthy.screen.dashboard.presentation

sealed interface DashboardAction {
    object Refresh : DashboardAction
    object EvaluateButtonClicked : DashboardAction
    data class ChartSelected(val index: Int) : DashboardAction

    object CloseBottomSheetClicked: DashboardAction

    data class CalculateButtonClicked(val amount: Float?): DashboardAction

}