package com.yusufteker.worthy.core.domain.model

import com.yusufteker.worthy.screen.card.domain.model.Card
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.expense
import worthy.composeapp.generated.resources.filter_none
import worthy.composeapp.generated.resources.filter_refund
import worthy.composeapp.generated.resources.income
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
enum class TransactionType {
    INCOME, EXPENSE, REFUND,
}

@Serializable
sealed class Transaction {
    abstract val id: Int
    abstract val originalId: Int
    abstract val name: String
    abstract val amount: Money
    abstract val transactionType: TransactionType
    abstract val categoryId: Int?
    abstract val cardId: Int?
    abstract val transactionDate: Long
    abstract val relatedTransactionId: Int?
    abstract val installmentCount: Int?

    abstract val installmentIndex: Int?
    //abstract val installmentStartDate: AppDate?
    abstract val note: String?

    @Serializable
    data class NormalTransaction(
        override val id: Int = 0,
        override val originalId: Int = id, // taksitliye çevirince normal id değişebilir
        override val name: String,
        override val amount: Money,
        override val transactionType: TransactionType,
        override val categoryId: Int?,
        override val cardId: Int? = null,
        override val transactionDate: Long,
        override val relatedTransactionId: Int? = null,
        override val installmentCount: Int? = null,
        override val installmentIndex: Int = -1,
        //override val installmentStartDate: AppDate? = null,
        override val note: String? = null
    ) : Transaction()

    @Serializable
    data class SubscriptionTransaction(
        override val id: Int = 0,
        override val originalId: Int = id,
        override val name: String,
        override val amount: Money,
        override val transactionType: TransactionType,
        override val categoryId: Int?,
        override val cardId: Int? = null,
        override val transactionDate: Long,
        override val relatedTransactionId: Int? = null,
        override val installmentCount: Int? = null,
        override val installmentIndex: Int = -1,
        //override val installmentStartDate: AppDate? = null,
        override val note: String? = null,
        val subscriptionId: Int,
        val subscriptionGroupId: String,
        val startDate: AppDate,
        val endDate: AppDate? = null,
        val colorHex: String? = null,
        ) : Transaction()


    @Serializable
    data class RecurringTransaction(
        override val id: Int = 0,
        override val originalId: Int = id,
        override val name: String,
        override val amount: Money,
        override val transactionType: TransactionType,
        override val categoryId: Int?,
        override val cardId: Int? = null,
        override val transactionDate: Long,
        override val relatedTransactionId: Int? = null,
        override val installmentCount: Int? = null,
        override val installmentIndex: Int = -1,
        //override val installmentStartDate: AppDate? = null,
        override val note: String? = null,
        val recurringGroupId: String,
        val month: Int,
        val year: Int
    ) : Transaction()
}

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

@OptIn(ExperimentalTime::class)
fun List<Transaction>.groupByMonth(): Map<String, List<Transaction>> {
    return this.groupBy { transaction ->
        val date = Instant.fromEpochMilliseconds(transaction.transactionDate)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        "${date.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${date.year}"
    }
}

fun Transaction.updateAmount(newAmount: Money): Transaction = when (this) {
    is Transaction.NormalTransaction -> this.copy(amount = newAmount)
    is Transaction.SubscriptionTransaction -> this.copy(amount = newAmount)
    is Transaction.RecurringTransaction -> this.copy(amount = newAmount)
}

fun Transaction.updateName(newName: String): Transaction = when (this) {
    is Transaction.NormalTransaction -> this.copy(name = newName)
    is Transaction.SubscriptionTransaction -> this.copy(name = newName)
    is Transaction.RecurringTransaction -> this.copy(name = newName)
}

fun Transaction.updateNote(newNote: String?): Transaction = when (this) {
    is Transaction.NormalTransaction -> this.copy(note = newNote)
    is Transaction.SubscriptionTransaction -> this.copy(note = newNote)
    is Transaction.RecurringTransaction -> this.copy(note = newNote)
}

fun Transaction.isSubscription(): Boolean {
    return this is Transaction.SubscriptionTransaction
}
fun Transaction.isRecurring(): Boolean {
    return this is Transaction.RecurringTransaction
}
fun Transaction.isNormal(): Boolean {
    return this is Transaction.NormalTransaction
}

fun Transaction.splitInstallments(card: Card?): List<Transaction> {
    if (installmentCount == null /*|| installmentStartDate == null*/ || (installmentCount ?: 0) <= 1) {
        return listOf(this)
    }

    val monthlyAmount = amount.divide(installmentCount?:1)
    val results = mutableListOf<Transaction>()

    val statementDay = card?.statementDay ?: 1 // statement day yoksa ayın 1’i gibi kabul edelim
    var currentDate = transactionDate.toAppDate()//installmentStartDate

    repeat(installmentCount?:1) { index ->
        // TransactionDate'i statement day'e göre ayarlıyoruz
        val txDate = currentDate!!.toEpochMillis(statementDay)

        results += when (this) {
            is Transaction.NormalTransaction -> copy(
                id = "${id}_$index".hashCode(), // benzersiz id üretmek için
                installmentIndex = index,
                amount = monthlyAmount,
                transactionDate = txDate
            )
            is Transaction.SubscriptionTransaction -> copy(
                id = "${id}_$index".hashCode(),
                installmentIndex = index,
                amount = monthlyAmount,
                transactionDate = txDate
            )
            is Transaction.RecurringTransaction -> copy(
                id = "${id}_$index".hashCode(),
                installmentIndex = index,

                amount = monthlyAmount,
                transactionDate = txDate
            )
        }

        // bir sonraki aya geç
        currentDate = currentDate.nextMonth()
    }

    return results
}

fun Transaction.isInstallment(): Boolean {
    return (this.installmentCount ?: 0) > 1
}

fun Transaction.getInstallmentLabel(index: Int? = null): String {
    val total = this.installmentCount ?: 1
    val current = index?.coerceIn(1..total) ?: 1
    return "$current/$total"
}


