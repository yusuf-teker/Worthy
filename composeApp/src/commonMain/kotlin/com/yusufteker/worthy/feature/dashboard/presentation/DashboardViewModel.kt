package com.yusufteker.worthy.feature.dashboard.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.core.domain.MonthlyAmount
import com.yusufteker.worthy.core.presentation.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel : BaseViewModel() {

    private val _state = MutableStateFlow(DashboardState(isLoading = true,))
    val state: StateFlow<DashboardState> = _state

    init { fetchDashboardData() }

    fun onAction(action: DashboardAction) = when (action) {
        DashboardAction.Refresh -> fetchDashboardData()
        DashboardAction.FabClicked -> null//navigateTo(Routes.ItemEvaluationScreen())
        is DashboardAction.ChartSelected -> {
            _state.update {
                it.copy(selectedChartIndex = if (state.value.selectedChartIndex != action.index) action.index else null)
            }
        }
    }

    private fun fetchDashboardData() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        // TODO: repository’den gerçek veriyi çek
        delay(800)    // demo loading
        _state.update {
            it.copy(
                userName = "Yusuf",
                monthlyIncome = 42000.0,
                monthlyExpense = 27000.0,
                savingProgress = 0.36f,
                isLoading = false,
                fixedExpenseFraction = 0.25f,
                desiresSpentFraction = 0.45f,
                expensesFraction = 0.3f,
                last6MonthAmounts = listOf(
                    MonthlyAmount("OCAK", 0.4f),
                    MonthlyAmount("SUBAT", 0.2f),
                    MonthlyAmount("MART", 0.3f),
                )
            )
        }
    }


}
