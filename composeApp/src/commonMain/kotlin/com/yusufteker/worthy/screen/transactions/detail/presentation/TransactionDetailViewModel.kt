package com.yusufteker.worthy.screen.transactions.detail.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.core.domain.model.emptyMoney
import com.yusufteker.worthy.core.domain.model.isActive
import com.yusufteker.worthy.core.domain.repository.CategoryRepository
import com.yusufteker.worthy.core.domain.repository.TransactionRepository
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

            is TransactionDetailAction.CreateCategory -> {
                viewModelScope.launch {
                    categoryRepository.insert(action.category)

                }
            }
            is TransactionDetailAction.UpdateCategory -> {
                _state.update { it.copy(selectedCategory = action.category) }
            }
            is TransactionDetailAction.UpdateAmount -> {
                _state.update { it.copy(transaction = it.transaction?.copy(amount = action.money?: emptyMoney())) }
            }
            is TransactionDetailAction.UpdateName -> {
                _state.update { it.copy(transaction = it.transaction?.copy(name = action.name)) }
            }
            is TransactionDetailAction.UpdateNote -> {
                _state.update { it.copy(transaction = it.transaction?.copy(note = action.note)) }
            }
        }
    }
}