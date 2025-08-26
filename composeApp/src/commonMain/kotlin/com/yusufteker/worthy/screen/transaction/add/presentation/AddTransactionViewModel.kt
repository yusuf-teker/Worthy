package com.yusufteker.worthy.screen.addtransaction.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.domain.model.Transaction
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.domain.model.toAppDate
import com.yusufteker.worthy.core.domain.repository.TransactionRepository
import com.yusufteker.worthy.core.presentation.UiEvent
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.ValidationResult
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
                                name = action.action.name
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

                    is TransactionFormAction.SaveClicked -> {

                        launchWithLoading {
                            if (validateExpenseForm(state.value).successful){
                                val transaction = Transaction(
                                    id = 0,
                                    name = _state.value.expenseForm.name,
                                    amount = _state.value.expenseForm.money!!,
                                    categoryId = _state.value.expenseForm.selectedCategory!!.id,
                                    transactionDate = _state.value.expenseForm.transactionDate,
                                    note = _state.value.expenseForm.note,
                                    installmentCount = _state.value.expenseForm.installmentCount,
                                    installmentStartDate = _state.value.expenseForm.installmentStartDate.toAppDate(),
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
                            if (validateIncomeForm(state.value).successful){
                                val transaction = Transaction(
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

private fun validateExpenseForm(state: AddTransactionState): ValidationResult {
    val form = state.expenseForm

    if (form.name.isBlank()) {
        return ValidationResult(
            successful = false,
            errorMessage = UiText.StringResourceId(Res.string.validation_name_empty)
        )
    }
    if (form.money == null) {
        return ValidationResult(
            successful = false,
            errorMessage = UiText.StringResourceId(Res.string.validation_amount_empty)
        )
    }
    if (form.selectedCategory == null) {
        return ValidationResult(
            successful = false,
            errorMessage = UiText.StringResourceId(Res.string.validation_category_empty)
        )
    }
    if (form.transactionDate == 0L) {
        return ValidationResult(
            successful = false,
            errorMessage = UiText.StringResourceId(Res.string.validation_date_empty)
        )
    }

    return ValidationResult(true)
}

private fun validateIncomeForm(state: AddTransactionState): ValidationResult {
    val form = state.incomeForm

    if (form.name.isBlank()) {
        return ValidationResult(
            successful = false,
            errorMessage = UiText.StringResourceId(Res.string.validation_name_empty)
        )
    }
    if (form.money == null) {
        return ValidationResult(
            successful = false,
            errorMessage = UiText.StringResourceId(Res.string.validation_amount_empty)
        )
    }
    if (form.selectedCategory == null) {
        return ValidationResult(
            successful = false,
            errorMessage = UiText.StringResourceId(Res.string.validation_category_empty)
        )
    }
    if (form.transactionDate == 0L) {
        return ValidationResult(
            successful = false,
            errorMessage = UiText.StringResourceId(Res.string.validation_date_empty)
        )
    }

    return ValidationResult(true)
}
