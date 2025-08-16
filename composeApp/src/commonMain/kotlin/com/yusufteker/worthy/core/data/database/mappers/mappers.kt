package com.yusufteker.worthy.core.data.database.mappers

import com.yusufteker.worthy.core.data.database.entities.CardEntity
import com.yusufteker.worthy.core.data.database.entities.CategoryEntity
import com.yusufteker.worthy.core.data.database.entities.RecurringFinancialItemEntity
import com.yusufteker.worthy.core.data.database.entities.TransactionEntity
import com.yusufteker.worthy.core.domain.getCurrentEpochMillis
import com.yusufteker.worthy.core.domain.getCurrentLocalDateTime
import com.yusufteker.worthy.core.domain.model.Card
import com.yusufteker.worthy.screen.wishlist.list.data.database.entities.WishlistItemEntity
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.RecurringFinancialItem
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.screen.wishlist.list.data.database.relation.WishlistWithCategory
import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistItem
import kotlin.time.Clock
import kotlin.time.ExperimentalTime





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


@OptIn(ExperimentalTime::class)
fun WishlistItem.toEntity(): WishlistItemEntity {
    val newItem = addedDate <= 0L
    return WishlistItemEntity(
        id = id,
        name = name,
        price = price,
        categoryId = category?.id,
        priority = priority,
        isPurchased = isPurchased,
        purchasedDate = purchasedDate,
        addedDate = if (newItem) Clock.System.now().epochSeconds else addedDate,
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
    startDate = startDate,
    endDate = endDate,
)

fun RecurringFinancialItem.toEntity(): RecurringFinancialItemEntity = RecurringFinancialItemEntity(
    id = id,
    groupId = groupId,
    name = name,
    amount = amount,
    isIncome = isIncome,
    needType = needType,
    scheduledDay = scheduledDay,
    startDate = startDate,
    endDate = endDate,
)



fun CardEntity.toDomain(): Card = Card(
    id = id,
    cardHolderName = cardHolderName,
    cardNumber = encryptedCardNumber,
    expiryMonth = expiryMonth,
    expiryYear = expiryYear,
    cvv = encryptedCvv,
    nickname = nickname,
    issuer = issuer,
    note = note
)

fun Card.toEntity(): CardEntity = CardEntity(
    id = id,
    cardHolderName = cardHolderName,
    encryptedCardNumber = cardNumber,
    expiryMonth = expiryMonth,
    expiryYear = expiryYear,
    encryptedCvv = cvv,
    nickname = nickname,
    issuer = issuer,
    note = note
)

// Entity -> Domain
fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = this.id,
        name = this.name,
        amount = this.amount,
        transactionType = this.transactionType,
        categoryId = this.categoryId,
        cardId = this.cardId,
        transactionDate = this.transactionDate,
        relatedTransactionId = this.relatedTransactionId,
        installmentCount = this.installmentCount,
        installmentStartDate = this.installmentStartDate
    )
}

// Domain -> Entity
fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = this.id,
        name = this.name,
        amount = this.amount,
        transactionType = this.transactionType,
        categoryId = this.categoryId,
        cardId = this.cardId,
        transactionDate = this.transactionDate,
        relatedTransactionId = this.relatedTransactionId,
        installmentCount = this.installmentCount,
        installmentStartDate = this.installmentStartDate
    )
}
fun WishlistItem.toExpenseTransaction(): Transaction {
    return Transaction(
        id = this.id,
        name = this.name,
        amount = this.price,
        transactionType = TransactionType.EXPENSE,
        categoryId = this.category?.id,
        cardId = null, //todo satın alırken card eklenebilir sonra param ile maplerim
        transactionDate = this.purchasedDate ?: getCurrentEpochMillis(),
        relatedTransactionId = null,
        installmentCount = null,
        installmentStartDate = null
    )
}