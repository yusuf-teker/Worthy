package com.yusufteker.worthy.screen.transactions.detail.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.core.domain.model.Transaction

import com.yusufteker.worthy.core.domain.model.updateAmount
import com.yusufteker.worthy.core.domain.model.updateName
import com.yusufteker.worthy.core.domain.model.updateNote
import com.yusufteker.worthy.core.domain.repository.CategoryRepository
import com.yusufteker.worthy.core.domain.repository.TransactionRepository
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.core.presentation.util.emptyMoney
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionDetailViewModel(
    val transactionRepository: TransactionRepository, val categoryRepository: CategoryRepository
) : BaseViewModel<TransactionDetailState>(TransactionDetailState()) {

    init {
        observeData()
    }

    fun observeData() {
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

            is TransactionDetailAction.UpdateTransaction -> {
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
                _state.update { state ->
                    state.copy(
                        transaction = state.transaction?.updateAmount(action.money ?: emptyMoney())
                    )
                }
            }

            is TransactionDetailAction.UpdateName -> {
                _state.update { it.copy(transaction = it.transaction?.updateName(action.name)) }
            }

            is TransactionDetailAction.UpdateNote -> {
                _state.update { it.copy(transaction = it.transaction?.updateNote(action.note)) }
            }

            is TransactionDetailAction.DeleteTransaction -> {
                launchWithLoading {
                    when(action.transaction) {
                        is Transaction.NormalTransaction -> {
                            transactionRepository.deleteByOriginalId(action.transaction.originalId)
                            navigateBack()
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}

