package com.yusufteker.worthy.screen.transaction.list.presentation

import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TransactionListViewModel : BaseViewModel<TransactionListState>(TransactionListState()) {

    fun onAction(action: TransactionListAction) {
        when (action) {
            is TransactionListAction.Init -> {
                // TODO
            }
        }
    }
}