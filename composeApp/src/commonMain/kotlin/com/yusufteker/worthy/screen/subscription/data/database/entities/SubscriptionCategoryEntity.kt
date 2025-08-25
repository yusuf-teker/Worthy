package com.yusufteker.worthy.screen.subscription.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yusufteker.worthy.core.data.database.mappers.toEntity
import com.yusufteker.worthy.screen.subscription.domain.model.defaultSubscriptionCategories

@Entity(tableName = "subscription_categories")
data class SubscriptionCategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,          // Örn: "Video", "Music"
    val nameResourceKey: String? = null, // String olarak resource key
    val icon: String,          // Örn: 🎬, 🎵
    val colorHex: String? = null
)

val defaultSubscriptionCategoriesEntities = defaultSubscriptionCategories.map {
    it.toEntity()
}
