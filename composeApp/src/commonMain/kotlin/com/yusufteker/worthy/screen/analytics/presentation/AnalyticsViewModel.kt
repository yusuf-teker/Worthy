package com.yusufteker.worthy.screen.analytics.presentation

import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AnalyticsViewModel : BaseViewModel<AnalyticsState>(AnalyticsState()) {

    fun onAction(action: AnalyticsAction) {
        when (action) {
            is AnalyticsAction.Init -> {
                // TODO
            }
        }
    }
}