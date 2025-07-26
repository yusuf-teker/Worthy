package com.yusufteker.worthy.screen.wishlist.add.presentation

import androidx.compose.ui.graphics.ImageBitmap

data class WishlistAddState(

    val imageBitmap: ImageBitmap? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)