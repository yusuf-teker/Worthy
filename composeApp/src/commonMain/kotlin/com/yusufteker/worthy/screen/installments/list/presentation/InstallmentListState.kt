package com.yusufteker.worthy.screen.installments.list.presentation

import com.yusufteker.worthy.core.presentation.base.BaseState
import com.yusufteker.worthy.screen.installments.list.domain.model.InstallmentMonthUIModel

data class InstallmentListState(
    override val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val monthGroups: List<InstallmentMonthUIModel> = emptyList(),
) : BaseState {
    override fun copyWithLoading(isLoading: Boolean): BaseState {
        return this.copy(isLoading = isLoading)
    }
}