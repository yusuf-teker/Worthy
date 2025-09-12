package com.yusufteker.worthy.screen.transactions.detail.presentation

import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.Transaction

sealed interface TransactionDetailAction {
    data class Init(val transactionId: Int) : TransactionDetailAction
    object NavigateBack : TransactionDetailAction
    data class UpdateTransaction(val transaction: Transaction): TransactionDetailAction

    data class UpdateCategory(val category: Category): TransactionDetailAction

    data class UpdateCard(val cardId: Int): TransactionDetailAction

    data object NavigateToAddCardScreen: TransactionDetailAction

    data class UpdateName(val name: String): TransactionDetailAction

    data class UpdateAmount(val money: Money?): TransactionDetailAction

    data class UpdateNote(val note: String): TransactionDetailAction

    data class UpdateDate(val date: Long): TransactionDetailAction

    data class CreateCategory(val category: Category): TransactionDetailAction

    data class DeleteTransaction(val transaction: Transaction): TransactionDetailAction

}