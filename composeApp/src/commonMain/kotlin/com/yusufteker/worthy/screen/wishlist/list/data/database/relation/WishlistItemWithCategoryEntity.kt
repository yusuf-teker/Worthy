package com.yusufteker.worthy.screen.wishlist.list.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.yusufteker.worthy.core.data.mappers.toDomain
import com.yusufteker.worthy.screen.wishlist.list.data.database.entities.WishlistCategoryEntity
import com.yusufteker.worthy.screen.wishlist.list.data.database.entities.WishlistItemEntity

//WishlistItemWithCategoryEntity DB de geçerli sadece
data class WishlistItemWithCategoryEntity(
    @Embedded val item: WishlistItemEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: WishlistCategoryEntity?
) {
    fun toDomain() = item.toDomain(category)
}
