package com.yusufteker.worthy.screen.wishlist.list.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.yusufteker.worthy.core.data.database.entities.CategoryEntity
import com.yusufteker.worthy.screen.wishlist.list.data.database.entities.WishlistItemEntity

data class WishlistWithCategory(
    @Embedded val item: WishlistItemEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity?
)