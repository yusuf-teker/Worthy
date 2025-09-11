package com.yusufteker.worthy.core.data.database.mappers

import com.yusufteker.worthy.screen.card.data.database.entities.CardEntity
import com.yusufteker.worthy.core.data.database.entities.CategoryEntity
import com.yusufteker.worthy.core.data.database.entities.RecurringFinancialItemEntity
import com.yusufteker.worthy.core.data.database.entities.TransactionEntity
import com.yusufteker.worthy.core.domain.getCurrentAppDate
import com.yusufteker.worthy.core.domain.getCurrentEpochMillis
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.screen.card.domain.model.Card
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.RecurringItem
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.domain.model.toEpochMillis
import com.yusufteker.worthy.screen.subscription.data.database.entities.SubscriptionEntity
import com.yusufteker.worthy.screen.wishlist.list.data.database.entities.WishlistItemEntity
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
        addedDate = if (newItem) Clock.System.now().toEpochMilliseconds() else addedDate,
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
        nameResourceKey = nameResourceKey,
        createdAt = createdAt,
        userCreated = userCreated,
        icon = icon,
        colorHex = colorHex
    )
}

// DB entity → Domain model (RecurringItem.Generic)
fun RecurringFinancialItemEntity.toDomain(): RecurringItem.Generic = RecurringItem.Generic(
    id = id,
    groupId = groupId,
    name = name,
    amount = amount,
    isIncome = isIncome,
    needType = needType,
    startDate = startDate,
    endDate = endDate,
    scheduledDay = scheduledDay,
    category = category
)

// Domain model → DB entity (RecurringItem.Generic)
fun RecurringItem.Generic.toEntity(): RecurringFinancialItemEntity = RecurringFinancialItemEntity(
    id = id,
    groupId = groupId,
    name = name,
    amount = amount,
    needType = needType,
    isIncome = isIncome,
    startDate = startDate,
    endDate = endDate,
    scheduledDay = scheduledDay,
    category = category
)



fun CardEntity.toDomain(): Card = Card(
    id = id,
    cardHolderName = cardHolderName,
    cardNumber = encryptedCardNumber,
    expiryMonth = expiryMonth,
    expiryYear = expiryYear,
    cvv = encryptedCvv,
    nickname = nickname,
    cardBrand = cardBrand,
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
    cardBrand = cardBrand,
    note = note
)

// Entity -> Domain
fun TransactionEntity.toDomain(): Transaction.NormalTransaction {
    return Transaction.NormalTransaction(
        id = id,
        originalId = originalId,
        name = name,
        amount = amount,
        transactionType = transactionType,
        categoryId = categoryId,
        cardId = cardId,
        transactionDate = transactionDate,
        relatedTransactionId = relatedTransactionId,
        installmentCount = installmentCount,
        //installmentStartDate = installmentStartDate,
        note = note
    )
}

// Domain -> Entity
fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = this.id,
        originalId = originalId,
        name = this.name,
        amount = this.amount,
        transactionType = this.transactionType,
        categoryId = this.categoryId,
        cardId = this.cardId,
        transactionDate = this.transactionDate,
        relatedTransactionId = this.relatedTransactionId,
        installmentCount = this.installmentCount,
        //installmentStartDate = this.installmentStartDate,
        note = this.note
    )
}

fun WishlistItem.toExpenseTransaction(): Transaction.NormalTransaction {
    return Transaction.NormalTransaction(
        id = this.id,
        originalId = this.id,// Todo simdilik taksit yok
        name = this.name,
        amount = this.price,
        transactionType = TransactionType.EXPENSE,
        categoryId = this.category?.id,
        cardId = null,
        transactionDate = this.purchasedDate ?: getCurrentEpochMillis(),
        relatedTransactionId = null,
        installmentCount = null,
        //installmentStartDate = null,
        note = null
    )
}

// SubscriptionEntity -> RecurringItem.Subscription
fun SubscriptionEntity.toDomain(): RecurringItem.Subscription = RecurringItem.Subscription(
    id = this.id,
    name = this.name,
    amount = this.money,
    startDate = this.startDate,
    endDate = this.endDate,
    scheduledDay = this.scheduledDay,
    cardId = this.cardId,
    icon = this.icon,
    colorHex = this.color,
    groupId = this.groupId,
    category = this.category
)


// RecurringItem.Subscription -> SubscriptionEntity
fun RecurringItem.Subscription.toEntity(): SubscriptionEntity = SubscriptionEntity(
    id = this.id,
    groupId = this.groupId,
    name = this.name,
    icon = this.icon,
    color = this.colorHex,
    category = this.category,
    money = this.amount,
    startDate = this.startDate,
    endDate = this.endDate,
    scheduledDay = this.scheduledDay,
    cardId = this.cardId
)

fun RecurringItem.Subscription.toTransactions(): List<Transaction> {
    val transactions = mutableListOf<Transaction>()

    val start = this.startDate
    val end = this.endDate ?: getCurrentAppDate(day = 1) // endDate null ise bugünü kullan

    var currentYear = start.year
    var currentMonth = start.month

    while (currentYear < end.year || (currentYear == end.year && currentMonth <= end.month)) {
        val transactionDate = AppDate(
            year = currentYear,
            month = currentMonth,
            day = this.scheduledDay ?: start.day
        )

        transactions.add(
            Transaction.SubscriptionTransaction(
                id ="${this.id}-${currentYear}-${currentMonth}".hashCode(),
                originalId = this.id, // todo subscription taksit yok şimdilik - transaction split fonksiyonu için gerekli değil
                name = this.name,
                amount = this.amount,
                transactionType = if (this.isIncome) TransactionType.INCOME else TransactionType.EXPENSE,
                categoryId = this.category?.id,
                cardId = this.cardId,
                transactionDate = transactionDate.toEpochMillis(),
                relatedTransactionId = null,
                installmentCount = null,
                //installmentStartDate = null,
                note = null,
                subscriptionGroupId = this.groupId,
                subscriptionId = this.id,
                startDate = this.startDate,
                endDate = this.endDate,
                colorHex = this.colorHex
            )
        )

        // sonraki aya geç
        if (currentMonth == 12) {
            currentMonth = 1
            currentYear++
        } else {
            currentMonth++
        }
    }

    return transactions
}

// Listeyi çevirmek için
fun List<RecurringItem.Subscription>.toTransactions(): List<Transaction> {
    return this.flatMap { it.toTransactions() }
}

