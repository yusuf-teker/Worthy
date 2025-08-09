package com.yusufteker.worthy.screen.addtransaction.presentation

import com.yusufteker.worthy.core.domain.repository.ExpenseRepository
import com.yusufteker.worthy.core.domain.repository.IncomeRepository
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddTransactionViewModel(
    private val expenseRepository: ExpenseRepository,
    private val incomeRepository: IncomeRepository
) : BaseViewModel() {
    private val _state = MutableStateFlow(AddTransactionState())
    val state: StateFlow<AddTransactionState> = _state

    fun onAction(action: AddTransactionAction) {
        when (action) {
            is AddTransactionAction.Init -> {
                // TODO
            }
        }
    }
}