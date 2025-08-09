package com.yusufteker.worthy.screen.addtransaction.presentation

import com.yusufteker.worthy.core.presentation.base.BaseState

data class AddTransactionState(
    override val isLoading: Boolean = false,
    val errorMessage: String? = null
): BaseState