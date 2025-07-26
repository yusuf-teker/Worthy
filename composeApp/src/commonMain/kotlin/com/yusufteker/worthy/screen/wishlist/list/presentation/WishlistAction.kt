package com.yusufteker.worthy.screen.wishlist.list.presentation

sealed interface WishlistAction {


    data class OnSearchTextChange(val text: String) : WishlistAction
    object OnSearch : WishlistAction
    object OnClearSearch : WishlistAction

    object OnFabClick : WishlistAction


}