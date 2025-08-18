package com.yusufteker.worthy.screen.wallet.presentation

import com.yusufteker.worthy.core.presentation.base.BaseViewModel

class WalletViewModel : BaseViewModel<WalletState>(WalletState()) {
    fun onAction(action: WalletAction) {
        when (action) {
            is WalletAction.Init -> {
                // TODO
            }
        }
    }
}