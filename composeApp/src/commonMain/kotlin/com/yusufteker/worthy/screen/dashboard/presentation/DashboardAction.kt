package com.yusufteker.worthy.screen.dashboard.presentation

import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.Transaction

sealed interface DashboardAction {

    object EvaluateButtonClicked : DashboardAction
    data class ChartSelected(val index: Int) : DashboardAction

    object CloseBottomSheetClicked : DashboardAction

    data class CalculateButtonClicked(val money: Money) : DashboardAction

    data class PurchaseButtonClicked(val expense: Transaction) : DashboardAction

    data class OnSelectedMonthChanged(val appDate: AppDate) : DashboardAction

    object AddWishlistClicked : DashboardAction
    object AddRecurringClicked : DashboardAction
    object AddTransactionClicked : DashboardAction

    object onInstallmentsMenuClicked : DashboardAction

    object onTransactionsMenuClicked: DashboardAction

}