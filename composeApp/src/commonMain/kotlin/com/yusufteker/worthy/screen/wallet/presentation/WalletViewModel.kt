package com.yusufteker.worthy.screen.wallet.presentation

import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WalletViewModel : BaseViewModel<WalletState>(WalletState()) {
    fun onAction(action: WalletAction) {
        when (action) {
            is WalletAction.Init -> {
                // TODO
            }
        }
    }
}