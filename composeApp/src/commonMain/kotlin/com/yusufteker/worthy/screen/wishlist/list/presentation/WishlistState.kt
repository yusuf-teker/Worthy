package com.yusufteker.worthy.screen.wishlist.list.presentation

import com.yusufteker.worthy.core.presentation.components.SearchResult
import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistItem

data class WishlistState(

    val items: List<WishlistItem> = emptyList(),
    val searchText: String = "",
    val searchHistory: List<String> = emptyList(),
    val searchResults: List<SearchResult> = emptyList(),
    val filteredItems: List<WishlistItem> = emptyList(),

    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
