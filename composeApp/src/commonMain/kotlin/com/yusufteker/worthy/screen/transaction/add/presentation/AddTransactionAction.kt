package com.yusufteker.worthy.screen.addtransaction.presentation

import com.yusufteker.worthy.core.domain.model.Card
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Money

sealed interface AddTransactionAction {
    object Init : AddTransactionAction

    data class ExpenseFormAction(val action: TransactionFormAction) : AddTransactionAction
    data class IncomeFormAction(val action: TransactionFormAction) : AddTransactionAction

    object OnBackClick : AddTransactionAction
}

sealed interface TransactionFormAction {

    data class NameChanged(val name: String) : TransactionFormAction
    data class MoneyChanged(val money: Money) : TransactionFormAction
    data class CategoryChanged(val category: Category) : TransactionFormAction
    data class DateChanged(val date: Long) : TransactionFormAction
    data class NoteChanged(val note: String) : TransactionFormAction
    data class CardSelected(val card: Card) : TransactionFormAction

    object AddNewCardClicked : TransactionFormAction
    data class InstallmentCountChanged(val count: Int) : TransactionFormAction
    data class InstallmentStartDateChanged(val date: Long) : TransactionFormAction
    object SaveClicked : TransactionFormAction
}