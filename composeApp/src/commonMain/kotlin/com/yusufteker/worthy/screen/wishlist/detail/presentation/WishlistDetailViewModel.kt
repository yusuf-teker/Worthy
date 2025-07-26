package com.yusufteker.worthy.screen.wishlist.detail.presentation

import com.yusufteker.worthy.core.presentation.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WishlistDetailViewModel : BaseViewModel() {
    private val _state = MutableStateFlow(WishlistDetailState())
    val state: StateFlow<WishlistDetailState> = _state

    fun onAction(action: WishlistDetailAction) {
        when (action) {
            is WishlistDetailAction.Init -> {
                // TODO
            }
        }
    }
}