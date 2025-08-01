package com.yusufteker.worthy.core.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yusufteker.worthy.core.data.database.mappers.toEntity
import com.yusufteker.worthy.core.domain.model.CategoryType
import com.yusufteker.worthy.core.domain.model.defaultCategories

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val nameResourceKey: String? = null,
    val type: CategoryType,
    val createdAt: Long,
    val userCreated: Boolean,
    val icon: String?,
    val colorHex: String?
)

val defaultCategoryEntities = defaultCategories.map {
    it.toEntity()
}