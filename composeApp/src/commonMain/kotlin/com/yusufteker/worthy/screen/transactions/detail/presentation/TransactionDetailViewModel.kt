package com.yusufteker.worthy.screen.transactions.detail.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.core.domain.model.isActive
import com.yusufteker.worthy.core.domain.repository.CategoryRepository
import com.yusufteker.worthy.core.domain.repository.TransactionRepository
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlin.collections.component1
import kotlin.collections.component2

class TransactionDetailViewModel(
    val transactionRepository: TransactionRepository,
    val categoryRepository: CategoryRepository
) : BaseViewModel<TransactionDetailState>(TransactionDetailState()) {

    init {
        observeData()
    }
    fun observeData(){
        launchWithLoading {
            categoryRepository.getAll().onEach { categories ->
                _state.update {
                    it.copy(
                        categories = categories
                    )
                }
            }.launchIn(viewModelScope)
        }

    }
    fun onAction(action: TransactionDetailAction) {
        when (action) {
            is TransactionDetailAction.Init -> {
                launchWithLoading {
                    _state.update {
                      it.copy(
                          transaction = transactionRepository.getById(action.transactionId),
                          selectedCategory = categoryRepository.getById(action.transactionId)
                      )
                    }
                }
            }
            is TransactionDetailAction.NavigateBack -> {
                navigateBack()
            }

            is TransactionDetailAction.UpdateTransaction ->  {
                launchWithLoading {
                    transactionRepository.update(action.transaction)
                }
            }

            is TransactionDetailAction.CreateCategory -> TODO()
            is TransactionDetailAction.UpdateCategory -> TODO()
        }
    }
}