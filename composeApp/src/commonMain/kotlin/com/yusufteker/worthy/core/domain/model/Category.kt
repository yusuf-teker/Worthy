package com.yusufteker.worthy.core.domain.model


data class Category(
    val id: Int = 0,
    val name: String,
    val type: CategoryType,
    val createdAt: Long,
    val userCreated: Boolean = true
)


enum class CategoryType {
    INCOME,
    EXPENSE,
    WISHLIST
}