package com.yusufteker.worthy.core.data.database.mappers

import com.yusufteker.worthy.core.data.database.entities.CategoryEntity
import com.yusufteker.worthy.core.data.database.entities.ExpenseEntity
import com.yusufteker.worthy.core.data.database.entities.IncomeEntity
import com.yusufteker.worthy.core.data.database.entities.RecurringFinancialItemEntity
import com.yusufteker.worthy.screen.wishlist.list.data.database.entities.WishlistItemEntity
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Expense
import com.yusufteker.worthy.core.domain.model.Income
import com.yusufteker.worthy.core.domain.model.RecurringFinancialItem
import com.yusufteker.worthy.screen.wishlist.list.data.database.relation.WishlistWithCategory
import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistItem
import kotlin.time.ExperimentalTime

// Expense
fun ExpenseEntity.toDomain() = Expense(
    id = id,
    name = name,
    amount = amount,
    categoryId = categoryId,
    scheduledDay = scheduledDay,
    needType = needType,
    isFixed = isFixed,
    date = date,
    note = note
)

fun Expense.toEntity() = ExpenseEntity(
    id = id,
    name = name,
    amount = amount,
    categoryId = categoryId,
    scheduledDay = scheduledDay,
    needType = needType,
    isFixed = isFixed,
    date = date,
    note = note
)

// Income
fun IncomeEntity.toDomain() = Income(
    id = id,
    name = name,
    amount = amount,
    categoryId = categoryId,
    scheduledDay = scheduledDay,
    isFixed = isFixed,
    date = date,
    note = note
)

fun Income.toEntity() = IncomeEntity(
    id = id,
    name = name,
    amount = amount,
    categoryId = categoryId,
    isFixed = isFixed,
    scheduledDay = scheduledDay,
    date = date,
    note = note
)

// WishlistItem
// data/mapper/WishlistMapper.kt

fun WishlistWithCategory.toDomain(): WishlistItem {
    return WishlistItem(
        id = item.id,
        name = item.name,
        price = item.price,
        priority = item.priority,
        isPurchased = item.isPurchased,
        addedDate = item.addedDate,
        note = item.note,
        imageUri = item.imageUri,
        category = category?.toDomain() // CategoryEntity → Category
    )
}

fun WishlistItemEntity.toDomain(category: Category?): WishlistItem {
    return WishlistItem(
        id = id,
        name = name,
        price = price,
        category = category,
        priority = priority,
        isPurchased = isPurchased,
        addedDate = addedDate,
        note = note,
        imageUri = imageUri
    )
}


fun WishlistItem.toEntity(): WishlistItemEntity {
    return WishlistItemEntity(
        id = id,
        name = name,
        price = price,
        categoryId = category?.id,
        priority = priority,
        isPurchased = isPurchased,
        addedDate = addedDate,
        note = note,
        imageUri = imageUri
    )
}



fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        nameResourceKey = nameResourceKey,
        type = type,
        createdAt = createdAt,
        userCreated = userCreated,
        icon = icon,
        colorHex = colorHex
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        type = type,
        createdAt = createdAt,
        userCreated = userCreated,
        icon = icon,
        colorHex = colorHex
    )
}



fun RecurringFinancialItemEntity.toDomain(): RecurringFinancialItem = RecurringFinancialItem(
    id = id,
    groupId = groupId,
    name = name,
    amount = amount,
    isIncome = isIncome,
    needType = needType,
    scheduledDay = scheduledDay,
    startMonth = startMonth,
    startYear = startYear,
    endMonth = endMonth,
    endYear = endYear,
)

fun RecurringFinancialItem.toEntity(): RecurringFinancialItemEntity = RecurringFinancialItemEntity(
    id = id,
    groupId = groupId,
    name = name,
    amount = amount,
    isIncome = isIncome,
    needType = needType,
    scheduledDay = scheduledDay,
    startMonth = startMonth,
    startYear = startYear,
    endMonth = endMonth,
    endYear = endYear,
)
