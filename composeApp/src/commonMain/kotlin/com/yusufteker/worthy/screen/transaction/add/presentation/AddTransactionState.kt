package com.yusufteker.worthy.screen.addtransaction.presentation

import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.BaseState
import com.yusufteker.worthy.screen.transaction.add.presentation.components.AddTransactionFormState

data class AddTransactionState(
    override val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isIncomeByDefault: Boolean = false,
    val expenseForm: AddTransactionFormState = AddTransactionFormState(isLoading = isLoading),
    val incomeForm: AddTransactionFormState = AddTransactionFormState(isLoading = isLoading)



    ) : BaseState {
    override fun copyWithLoading(isLoading: Boolean) = copy(isLoading = isLoading)

}