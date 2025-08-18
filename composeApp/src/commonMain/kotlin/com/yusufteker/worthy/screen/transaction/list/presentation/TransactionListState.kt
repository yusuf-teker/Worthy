package com.yusufteker.worthy.screen.transaction.list.presentation

import com.yusufteker.worthy.core.presentation.base.BaseState

data class TransactionListState(
    override val isLoading: Boolean = false,
    val errorMessage: String? = null
) : BaseState {
    override fun copyWithLoading(isLoading: Boolean): BaseState = copy(isLoading = isLoading)
}