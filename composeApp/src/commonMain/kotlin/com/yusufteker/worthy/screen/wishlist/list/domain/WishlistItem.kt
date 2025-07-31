package com.yusufteker.worthy.screen.wishlist.list.domain

import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money

data class WishlistItem(
    val id: Int = 0,
    val name: String = "",
    val price: Money = Money(0.0, Currency.TRY),
    val category: Category? = null,
    val priority: Int = 0,
    val isPurchased: Boolean = false,
    val addedDate: Long = -1,
    val note: String? = "",
    val imageUri: String? = ""
)