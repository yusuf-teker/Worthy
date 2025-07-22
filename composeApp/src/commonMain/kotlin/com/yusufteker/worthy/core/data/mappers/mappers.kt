package com.yusufteker.worthy.core.data.mappers

import com.yusufteker.worthy.core.data.database.entities.CategoryEntity
import com.yusufteker.worthy.core.data.database.entities.ExpenseEntity
import com.yusufteker.worthy.core.data.database.entities.IncomeEntity
import com.yusufteker.worthy.core.data.database.entities.RecurringFinancialItemEntity
import com.yusufteker.worthy.core.data.database.entities.WishlistItemEntity
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Expense
import com.yusufteker.worthy.core.domain.model.Income
import com.yusufteker.worthy.core.domain.model.RecurringFinancialItem
import com.yusufteker.worthy.core.domain.model.WishlistItem
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
fun WishlistItemEntity.toDomain() = WishlistItem(
    id = id,
    name = name,
    price = price,
    categoryId = categoryId,
    priority = priority,
    isPurchased = isPurchased,
    addedDate = addedDate,
    note = note
)

fun WishlistItem.toEntity() = WishlistItemEntity(
    id = id,
    name = name,
    price = price,
    categoryId = categoryId,
    priority = priority,
    isPurchased = isPurchased,
    addedDate = addedDate,
    note = note
)

// Category
@OptIn(ExperimentalTime::class)
fun CategoryEntity.toDomain() = Category(
    id = id,
    name = name,
    type = type,
    createdAt = createdAt,
    userCreated = userCreated
)

fun Category.toEntity() = CategoryEntity(
    id = id,
    name = name,
    type = type,
    createdAt = createdAt,
    userCreated = userCreated
)


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
