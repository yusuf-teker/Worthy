package com.yusufteker.worthy.screen.wishlist.detail.presentation

sealed interface WishlistDetailAction {
    data class Init(val wishlistItemId: Int? = null) : WishlistDetailAction
}