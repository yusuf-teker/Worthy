package com.yusufteker.worthy.screen.wishlist.add.presentation

import androidx.compose.ui.graphics.ImageBitmap
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.presentation.base.BaseState
import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistItem

data class WishlistAddState(

    val imageBitmap: ImageBitmap? = null,
    val errorMessage: String? = null,
    val wishlistItem: WishlistItem = WishlistItem(),

    val wishlistCategories : List<Category> = emptyList(),
    override val isLoading: Boolean = false,
): BaseState