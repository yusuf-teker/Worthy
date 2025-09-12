package com.yusufteker.worthy.screen.installments.list.data.repository

import com.yusufteker.worthy.core.data.database.mappers.toDomain
import com.yusufteker.worthy.core.data.database.model.TransactionDao
import com.yusufteker.worthy.core.domain.model.isInstallment
import com.yusufteker.worthy.core.domain.model.splitInstallments
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
            transactionDao.getAll().map { list -> list.map { it.toDomain() } },
            cardRepository.getAll()
        ) { transactions, cards ->
            transactions
                .filter { it.isInstallment() } // sadece taksitli işlemler
                .flatMap { tx ->
                    val card = cards.find { it.id == tx.cardId } // doğru kartı bul
                    tx.splitInstallments(card).map{ transaction ->
                        InstallmentCardUIModel(transaction, card)
                    }
                }
                .sortedByDescending { it.transaction.transactionDate }

        }
    }

    override suspend fun addSomething(item: String) {
        TODO("Not yet implemented")
    }
}