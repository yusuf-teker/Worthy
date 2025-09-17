package com.yusufteker.worthy.screen.installments.list.presentation

import com.yusufteker.worthy.core.domain.model.AppDate

sealed interface InstallmentListAction {
    object Init : InstallmentListAction
    object NavigateBack : InstallmentListAction
    data class OnMonthClicked(val month: AppDate): InstallmentListAction
}