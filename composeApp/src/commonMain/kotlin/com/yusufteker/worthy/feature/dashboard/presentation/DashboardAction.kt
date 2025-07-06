package com.yusufteker.worthy.feature.dashboard.presentation

sealed interface DashboardAction {
    object Refresh : DashboardAction
    object FabClicked : DashboardAction
    data class ChartSelected(val index: Int) : DashboardAction

}