package com.yusufteker.worthy.core.domain.model


enum class TransactionType {
    INCOME,
    EXPENSE,
    REFUND,
}

data class Transaction(
    val id: Int = 0,
    val name: String,
    val amount: Money,
    val transactionType: TransactionType,
    val categoryId: Int?,
    val cardId: Int? = null,
    val transactionDate: Long,
    val relatedTransactionId: Int? = null,
    val installmentCount: Int? = null,
    val installmentStartDate: AppDate? = null,
    val note: String? = null
)
