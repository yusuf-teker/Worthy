package com.yusufteker.worthy.screen.wishlist.list.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist_categories")
data class WishlistCategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val icon: String? = null,
    val colorHex: String? = null
)
