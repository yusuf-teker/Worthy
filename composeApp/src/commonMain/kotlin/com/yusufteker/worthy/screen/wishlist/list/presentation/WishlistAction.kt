package com.yusufteker.worthy.screen.wishlist.list.presentation

sealed interface WishlistAction {


    data class OnSearchTextChange(val text: String) : WishlistAction
    data class OnItemClick(val itemId: Int) : WishlistAction
    data class OnItemDelete(val itemId: Int) : WishlistAction
    object OnSearch : WishlistAction
    object OnClearSearch : WishlistAction

    object OnFabClick : WishlistAction


}