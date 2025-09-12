package com.yusufteker.worthy.screen.installments.list.presentation

sealed interface InstallmentListAction {
    object Init : InstallmentListAction
    object NavigateBack : InstallmentListAction
}