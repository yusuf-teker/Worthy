package com.yusufteker.worthy.screen.wishlist.list.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.yusufteker.worthy.core.data.database.entities.CategoryEntity
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money

@Entity(
    tableName = "wishlist",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.Companion.SET_NULL
    )],
    indices = [Index("categoryId")]
)
data class  WishlistItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val price: Money,
    val categoryId: Int?,
    val priority: Int = 0,
    val isPurchased: Boolean = false,
    val addedDate: Long,
    val purchasedDate: Long? = null,
    val note: String? = null,
    val imageUri: String? = null
)

