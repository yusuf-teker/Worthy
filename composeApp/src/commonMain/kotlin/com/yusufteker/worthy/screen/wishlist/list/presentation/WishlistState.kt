package com.yusufteker.worthy.screen.wishlist.list.presentation

import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistItem

data class WishlistState(

    val items: List<WishlistItem> = emptyList(),
    val searchText: String = "",

    val isLoading: Boolean = false,
    val errorMessage: String? = null
    )
