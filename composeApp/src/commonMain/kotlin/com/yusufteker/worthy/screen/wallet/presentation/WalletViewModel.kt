package com.yusufteker.worthy.screen.wallet.presentation

import com.yusufteker.worthy.core.presentation.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WalletViewModel : BaseViewModel() {
    private val _state = MutableStateFlow(WalletState())
    val state: StateFlow<WalletState> = _state

    fun onAction(action: WalletAction) {
        when (action) {
            is WalletAction.Init -> {
                // TODO
            }
        }
    }
}