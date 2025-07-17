package com.yusufteker.worthy.core.domain.model

data class WishlistItem(
    val id: Int = 0,
    val name: String,
    val price: Double,
    val categoryId: Int?,
    val priority: Int = 0,
    val isPurchased: Boolean = false,
    val addedDate: Long,
    val note: String? = null
)