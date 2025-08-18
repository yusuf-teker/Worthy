package com.yusufteker.worthy.screen.wishlist.detail.presentation

import com.yusufteker.worthy.core.presentation.base.BaseViewModel

class WishlistDetailViewModel : BaseViewModel<WishlistDetailState>(WishlistDetailState()) {

    fun onAction(action: WishlistDetailAction) {
        when (action) {
            is WishlistDetailAction.Init -> {
                // TODO
            }
        }
    }
}