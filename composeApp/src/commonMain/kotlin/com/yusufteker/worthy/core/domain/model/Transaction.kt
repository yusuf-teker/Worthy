package com.yusufteker.worthy.core.domain.model

import org.jetbrains.compose.resources.StringResource
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.expense
import worthy.composeapp.generated.resources.filter_none
import worthy.composeapp.generated.resources.filter_refund
import worthy.composeapp.generated.resources.income

enum class TransactionType {
    INCOME, EXPENSE, REFUND,
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
fun List<Transaction>.distinctCategoryIds(): List<Int> {
    return this.mapNotNull { it.categoryId }.distinct()
}

val TransactionType.labelRes: StringResource
    get() = when (this) {
        TransactionType.INCOME -> Res.string.income
        TransactionType.EXPENSE -> Res.string.expense
        TransactionType.REFUND -> Res.string.filter_refund
        else -> Res.string.filter_none
    }