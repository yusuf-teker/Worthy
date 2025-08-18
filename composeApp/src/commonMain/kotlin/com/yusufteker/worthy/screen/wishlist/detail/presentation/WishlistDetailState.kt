package com.yusufteker.worthy.screen.wishlist.detail.presentation

import com.yusufteker.worthy.core.presentation.base.BaseState

data class WishlistDetailState(
    override val isLoading: Boolean = false,
    val errorMessage: String? = null
): BaseState{
    override fun copyWithLoading(isLoading: Boolean): BaseState {
        return copy(isLoading = isLoading)
    }
}