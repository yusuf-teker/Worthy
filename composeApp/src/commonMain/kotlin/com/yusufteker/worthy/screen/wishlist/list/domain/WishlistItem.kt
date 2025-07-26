package com.yusufteker.worthy.screen.wishlist.list.domain

data class WishlistItem(
    val id: Int = 0,
    val name: String,
    val price: Double,
    val category: WishlistCategory?,
    val priority: Int = 0,
    val isPurchased: Boolean = false,
    val addedDate: Long,
    val note: String? = null,
    val imageUri: String?
)