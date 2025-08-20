package com.yusufteker.worthy.screen.wishlist.detail.presentation

import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.update

class WishlistDetailViewModel : BaseViewModel<WishlistDetailState>(WishlistDetailState()) {

    fun onAction(action: WishlistDetailAction) {
        when (action) {
            is WishlistDetailAction.Init -> {
                _state.update {
                    it.copy(wishlistItemId = action.wishlistItemId)
                }
            }
        }
    }
}