package com.yusufteker.worthy.screen.installments.list.data.repository

import com.yusufteker.worthy.core.data.database.mappers.toDomain
import com.yusufteker.worthy.core.data.database.model.TransactionDao
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.domain.model.splitInstallmentsByFirstPaymentDate
import com.yusufteker.worthy.screen.card.domain.repository.CardRepository
import com.yusufteker.worthy.screen.installments.list.domain.model.InstallmentCardUIModel
import com.yusufteker.worthy.screen.installments.list.domain.repository.InstallmentListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class InstallmentListRepositoryImpl(
    val transactionDao: TransactionDao,
    val cardRepository: CardRepository
): InstallmentListRepository {

    override fun getAllInstallments(): Flow<List<InstallmentCardUIModel>> {
        return combine(
            transactionDao.getExpenseAndRefundTransactions().map { list -> list.map { it.toDomain() } },
            cardRepository.getAll()
        ) { transactions, cards ->
            // 1. Taksitleri split et ve kartla eşleştir
            val splitInstallments = transactions.flatMap { tx ->
                val card = cards.find { it.id == tx.cardId }
                tx.splitInstallmentsByFirstPaymentDate(card).map { transaction ->
                    InstallmentCardUIModel(transaction, card)
                }
            }

            // 2. Refund transactionları merge et
            val mergedRefunds = splitInstallments
                .groupBy { it.transaction.originalId.takeIf { id -> id != 0 } ?: it.transaction.relatedTransactionId }
                .values
                .flatMap { group ->
                    val refunds = group.filter { it.transaction.transactionType == TransactionType.REFUND }
                    if (refunds.isNotEmpty()) {
                        // Refund varsa hepsini merge et, tek transaction yap
                        listOf(mergeRefundedInstallments(refunds))
                    } else {
                        // Normal taksitler olduğu gibi kalır
                        group
                    }
                }

            // 3. Tarihe göre sırala
            mergedRefunds.sortedByDescending { it.transaction.firstPaymentDate }
        }
    }

    override suspend fun addSomething(item: String) {
        TODO("Not yet implemented")
    }
}

private fun mergeRefundedInstallments(group: List<InstallmentCardUIModel>): InstallmentCardUIModel {
    val firstRefund = group.first()
    val totalAmount = group.sumOf { it.transaction.amount.amount }
    val currency = firstRefund.transaction.amount.currency

    val mergedTransaction = Transaction.NormalTransaction(
        id = firstRefund.transaction.id, // istersen yeni id üret
        originalId = firstRefund.transaction.originalId,
        name = firstRefund.transaction.name,
        amount = firstRefund.transaction.amount.copy(amount = totalAmount),
        transactionType = TransactionType.REFUND,
        categoryId = firstRefund.transaction.categoryId,
        cardId = firstRefund.transaction.cardId,
        transactionDate = firstRefund.transaction.transactionDate,
        relatedTransactionId = firstRefund.transaction.relatedTransactionId,
        installmentCount = 1,
        installmentIndex = 0,
        refundDate = firstRefund.transaction.refundDate,
        note = firstRefund.transaction.note,
        firstPaymentDate = firstRefund.transaction.firstPaymentDate
    )

    return InstallmentCardUIModel(mergedTransaction, firstRefund.card)
}