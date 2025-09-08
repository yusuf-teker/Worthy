package com.yusufteker.worthy.screen.addtransaction.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.domain.model.toAppDate
import com.yusufteker.worthy.core.domain.repository.TransactionRepository
import com.yusufteker.worthy.core.presentation.UiEvent
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.validation_amount_empty
import worthy.composeapp.generated.resources.validation_category_empty
import worthy.composeapp.generated.resources.validation_date_empty
import worthy.composeapp.generated.resources.validation_name_empty

class AddTransactionViewModel(
    private val transactionRepository: TransactionRepository,
) : BaseViewModel<AddTransactionState>(AddTransactionState()) {


    init {
        observeData()
    }

    fun observeData() {
        launchWithLoading {
            combine(
                transactionRepository.getExpenseCategories(),
                transactionRepository.getIncomeCategories(),
                transactionRepository.getCards()
            ) { expenseCategories, incomeCategories, cards ->
                _state.value = _state.value.copy(
                    expenseForm = _state.value.expenseForm.copy(
                        categories = expenseCategories,
                        cards = cards
                    ),
                    incomeForm = _state.value.incomeForm.copy(
                        categories = incomeCategories
                    )
                )
            }.launchIn(viewModelScope)

        }
    }


    fun onAction(action: AddTransactionAction) {
        when (action) {
            is AddTransactionAction.Init -> {

            }

            is AddTransactionAction.ExpenseFormAction -> {
                when (action.action) {
                    is TransactionFormAction.NameChanged -> {
                        _state.value = _state.value.copy(
                            expenseForm = _state.value.expenseForm.copy(
                                name = action.action.name,
                                errorName = null,
                            )
                        )
                    }

                    is TransactionFormAction.CardSelected -> {
                        _state.value = _state.value.copy(
                            expenseForm = _state.value.expenseForm.copy(
                                selectedCard = action.action.card
                            )
                        )
                    }
                    is TransactionFormAction.CategoryChanged -> {
                        _state.value = _state.value.copy(
                            expenseForm = _state.value.expenseForm.copy(
                                selectedCategory = action.action.category,
                                errorCategory = null
                            )
                        )
                    }

                    is TransactionFormAction.DateChanged -> {
                        _state.value = _state.value.copy(
                            expenseForm = _state.value.expenseForm.copy(
                                transactionDate = action.action.date,
                                errorDate = null
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
                                money = action.action.money,
                                errorMoney = null
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

                    is TransactionFormAction.SaveClicked -> {

                        launchWithLoading {
                            val validated = validateExpenseForm(state.value)
                            if (validated.expenseForm.errorName != null ||
                                validated.expenseForm.errorMoney != null ||
                                validated.expenseForm.errorCategory != null ||
                                validated.expenseForm.errorDate != null
                            ) {
                                _state.value = validated
                            } else {
                                val transaction = Transaction.NormalTransaction(
                                    id = 0,
                                    name = validated.expenseForm.name,
                                    amount = validated.expenseForm.money!!,
                                    categoryId = validated.expenseForm.selectedCategory!!.id,
                                    transactionDate = validated.expenseForm.transactionDate,
                                    note = validated.expenseForm.note,
                                    installmentCount = validated.expenseForm.installmentCount,
                                    installmentStartDate = validated.expenseForm.installmentStartDate.toAppDate(),
                                    transactionType = TransactionType.EXPENSE,
                                )
                                transactionRepository.insert(transaction)
                                navigateTo(Routes.AnalyticsGraph)
                            }

                        }

                    }
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
                                name = action.action.name,
                                errorMoney = null
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
                                selectedCategory = action.action.category,
                                errorCategory = null
                            )
                        )
                    }

                    is TransactionFormAction.DateChanged -> {
                        _state.value = _state.value.copy(
                            incomeForm = _state.value.incomeForm.copy(
                                transactionDate = action.action.date,
                                errorDate = null
                            )
                        )
                    }

                    is TransactionFormAction.MoneyChanged -> {
                        _state.value = _state.value.copy(
                            incomeForm = _state.value.incomeForm.copy(
                                money = action.action.money,
                                errorMoney = null
                            )
                        )
                    }

                    is TransactionFormAction.NoteChanged -> {
                        _state.value = _state.value.copy(
                            incomeForm = _state.value.incomeForm.copy(
                                note = action.action.note
                            )
                        )
                    }
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

                    is TransactionFormAction.SaveClicked -> {

                        launchWithLoading {
                            val validated = validateIncomeForm(state.value)
                            if (validated.incomeForm.errorName != null ||
                                validated.incomeForm.errorMoney != null ||
                                validated.incomeForm.errorCategory != null ||
                                validated.incomeForm.errorDate != null
                            ) {
                                _state.value = validated
                            } else {
                                val transaction = Transaction.NormalTransaction(
                                    id = 0,
                                    name = _state.value.incomeForm.name,
                                    amount = _state.value.incomeForm.money!!,
                                    categoryId = _state.value.incomeForm.selectedCategory!!.id,
                                    transactionDate = _state.value.incomeForm.transactionDate,
                                    note = _state.value.incomeForm.note,
                                    transactionType = TransactionType.INCOME,

                                    )

                                transactionRepository.insert(transaction)

                                navigateTo(Routes.AnalyticsGraph)
                            }

                        }


                    }
                    is TransactionFormAction.AddNewCardClicked -> {

                    }

                    is TransactionFormAction.IsCardPaymentChanged -> {

                    }
                }
            }

            is AddTransactionAction.OnBackClick -> {
                navigateBack()
            }
        }
    }
}
private fun validateExpenseForm(state: AddTransactionState): AddTransactionState {
    val form = state.expenseForm
    return state.copy(
        expenseForm = form.copy(
            errorName = if (form.name.isBlank()) UiText.StringResourceId(Res.string.validation_name_empty) else null,
            errorMoney = if (form.money == null) UiText.StringResourceId(Res.string.validation_amount_empty) else null,
            errorCategory = if (form.selectedCategory == null) UiText.StringResourceId(Res.string.validation_category_empty) else null,
            errorDate = if (form.transactionDate == 0L) UiText.StringResourceId(Res.string.validation_date_empty) else null
        )
    )
}



private fun validateIncomeForm(state: AddTransactionState): AddTransactionState {
    val form = state.incomeForm
    return state.copy(
        incomeForm = form.copy(
            errorName = if (form.name.isBlank()) UiText.StringResourceId(Res.string.validation_name_empty) else null,
            errorMoney = if (form.money == null) UiText.StringResourceId(Res.string.validation_amount_empty) else null,
            errorCategory = if (form.selectedCategory == null) UiText.StringResourceId(Res.string.validation_category_empty) else null,
            errorDate = if (form.transactionDate == 0L) UiText.StringResourceId(Res.string.validation_date_empty) else null
        )
    )
}
