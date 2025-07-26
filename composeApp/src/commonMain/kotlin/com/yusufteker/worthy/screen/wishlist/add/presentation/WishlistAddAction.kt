package com.yusufteker.worthy.screen.wishlist.add.presentation

import androidx.compose.ui.graphics.ImageBitmap

sealed interface WishlistAddAction {
    object Init : WishlistAddAction
    data class OnImageSelected (val bitmap: ImageBitmap) : WishlistAddAction

    object OnImageRemoved  : WishlistAddAction

    object OnWishlistAdd : WishlistAddAction


}