package com.yusufteker.worthy.screen.wallet.presentation

import com.yusufteker.worthy.core.presentation.base.BaseState

data class WalletState(
    override val isLoading: Boolean = false,
    val errorMessage: String? = null
): BaseState{
    override fun copyWithLoading(isLoading: Boolean): BaseState = copy(isLoading = isLoading)
}