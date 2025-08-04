package com.yusufteker.worthy.screen.wishlist.add.presentation

import androidx.compose.ui.graphics.ImageBitmap
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Money

sealed interface WishlistAddAction {
    data class OnImageSelected (val bitmap: ImageBitmap) : WishlistAddAction
    object OnWishlistAdd : WishlistAddAction

    data class OnPriorityChanged(val priority: Int) : WishlistAddAction

    data class OnCategorySelected(val wishlistCategory: Category)
        : WishlistAddAction

    data class OnNewCategoryCreated(val wishlistCategory: Category) : WishlistAddAction

    data class OnPriceChanged(val price: Money) : WishlistAddAction

    data class OnNameChanged(val name: String) : WishlistAddAction

    data class OnNoteChanged(val note: String) : WishlistAddAction

    data class OnPurchasedChanged(val isPurchased: Boolean) : WishlistAddAction

    object OnSaveClicked : WishlistAddAction

    object OnBackClick: WishlistAddAction



}