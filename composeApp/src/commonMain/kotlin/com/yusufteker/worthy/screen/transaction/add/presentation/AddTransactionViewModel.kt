package com.yusufteker.worthy.screen.addtransaction.presentation

import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.domain.repository.TransactionRepository
import com.yusufteker.worthy.core.presentation.UiEvent
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddTransactionViewModel(
    private val transactionRepository: TransactionRepository,
) : BaseViewModel() {
    private val _state = MutableStateFlow(AddTransactionState())
    val state: StateFlow<AddTransactionState> = _state

    fun onAction(action: AddTransactionAction) {
        when (action) {
            is AddTransactionAction.Init -> {

            }

            is AddTransactionAction.ExpenseFormAction -> {
                when (action.action) {
                    is TransactionFormAction.NameChanged -> {
                        _state.value = _state.value.copy(
                            expenseForm = _state.value.expenseForm.copy(
                                name = action.action.name
                            )
                        )
                    }

                    is TransactionFormAction.CardSelected -> {}
                    is TransactionFormAction.CategoryChanged -> {
                        _state.value = _state.value.copy(
                            expenseForm = _state.value.expenseForm.copy(
                                selectedCategory = action.action.category
                            )
                        )
                    }
                    is TransactionFormAction.DateChanged -> {
                        _state.value = _state.value.copy(
                            expenseForm = _state.value.expenseForm.copy(
                                transactionDate = action.action.date
                            )
                        )
                    }
                    is TransactionFormAction.InstallmentCountChanged -> {
                        _state.value = _state.value.copy(
                            expenseForm = _state.value.expenseForm.copy(
                                installmentCount = action.action.count
                            )
                        )
                    }
                    is TransactionFormAction.InstallmentStartDateChanged -> {
                        _state.value = _state.value.copy(
                            expenseForm = _state.value.expenseForm.copy(
                                installmentStartDate = action.action.date
                            )
                        )
                    }
                    is TransactionFormAction.MoneyChanged -> {
                        _state.value = _state.value.copy(
                            expenseForm = _state.value.expenseForm.copy(
                                money = action.action.money
                            )
                        )
                    }
                    is TransactionFormAction.NoteChanged -> {
                        _state.value = _state.value.copy(
                            expenseForm = _state.value.expenseForm.copy(
                                note = action.action.note
                            )
                        )

                    }
                    is TransactionFormAction.SaveClicked -> {}
                    is TransactionFormAction.AddNewCardClicked -> {
                        sendUiEventSafe(UiEvent.NavigateTo(Routes.AddCard))
                    }

                    is TransactionFormAction.IsCardPaymentChanged -> {
                        _state.value = _state.value.copy(
                            expenseForm = _state.value.expenseForm.copy(
                                isCardPayment = action.action.isCardPayment
                            )
                        )

                    }
                }

            }
            is AddTransactionAction.IncomeFormAction -> {
                when (action.action) {
                    is TransactionFormAction.NameChanged -> {
                        _state.value = _state.value.copy(
                            incomeForm = _state.value.incomeForm.copy(
                                name = action.action.name
                            )
                        )
                    }
                    is TransactionFormAction.CardSelected -> {
                        _state.value = _state.value.copy(
                            incomeForm = _state.value.incomeForm.copy(
                                selectedCard = action.action.card
                            )
                        )
                    }
                    is TransactionFormAction.CategoryChanged -> {
                        _state.value = _state.value.copy(
                            incomeForm = _state.value.incomeForm.copy(
                                selectedCategory = action.action.category
                            )
                        )
                    }
                    is TransactionFormAction.DateChanged -> {
                        _state.value = _state.value.copy(
                            incomeForm = _state.value.incomeForm.copy(
                                transactionDate = action.action.date
                            )
                        )
                    }
                    is TransactionFormAction.MoneyChanged -> {
                        _state.value = _state.value.copy(
                            incomeForm = _state.value.incomeForm.copy(
                                money = action.action.money
                            )
                        )
                    }
                    is TransactionFormAction.NoteChanged -> {}
                    is TransactionFormAction.InstallmentCountChanged -> {
                        _state.value = _state.value.copy(
                            incomeForm = _state.value.incomeForm.copy(
                                installmentCount = action.action.count
                            )
                        )
                    }
                    is TransactionFormAction.InstallmentStartDateChanged -> {
                        _state.value = _state.value.copy(
                            incomeForm = _state.value.incomeForm.copy(
                                installmentStartDate = action.action.date
                            )
                        )
                    }
                    TransactionFormAction.SaveClicked -> {}
                    TransactionFormAction.AddNewCardClicked -> {

                    }

                    is TransactionFormAction.IsCardPaymentChanged -> {

                    }
                }
            }

            AddTransactionAction.OnBackClick -> {
                sendUiEventSafe(UiEvent.NavigateBack)
            }
        }
    }
}