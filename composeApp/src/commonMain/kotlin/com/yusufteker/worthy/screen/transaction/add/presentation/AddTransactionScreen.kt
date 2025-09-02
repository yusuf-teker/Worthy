package com.yusufteker.worthy.screen.addtransaction.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.app.navigation.NavigationHandler
import com.yusufteker.worthy.app.navigation.NavigationModel
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
import com.yusufteker.worthy.core.presentation.components.AppTopBar
import com.yusufteker.worthy.core.presentation.components.Screen
import com.yusufteker.worthy.core.presentation.components.TabbedScreen
import com.yusufteker.worthy.screen.transaction.add.presentation.components.AddTransactionForm
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.add_expense
import worthy.composeapp.generated.resources.add_income
import worthy.composeapp.generated.resources.screen_title_new_transaction

@Composable
fun AddTransactionScreenRoot(
    viewModel: AddTransactionViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    isIncomeByDefault: Boolean = false,
    onNavigateTo: (NavigationModel) -> Unit = {}

) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NavigationHandler(viewModel) { model ->
        onNavigateTo(model)
    }


    BaseContentWrapper(
        state = state
    ) {
        AddTransactionScreen(
            modifier = it,
            state = state,
            onAction = viewModel::onAction,
            contentPadding = contentPadding,
            isIncomeByDefault = isIncomeByDefault
        )
    }

}

@Composable
fun AddTransactionScreen(
    modifier: Modifier = Modifier,
    state: AddTransactionState,
    onAction: (action: AddTransactionAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
    isIncomeByDefault: Boolean = false

) {
    Column(
        modifier = modifier.padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        AppTopBar(
            title = UiText.StringResourceId(Res.string.screen_title_new_transaction).asString(),
            onNavIconClick = { onAction(AddTransactionAction.OnBackClick) },
            isBack = true
        )

        val addIncomeScreen = Screen(
            title = UiText.StringResourceId(Res.string.add_income).asString(), content = {
                AddTransactionForm(
                    state = state.incomeForm,
                    isExpense = false,
                    onAmountChange = {
                        onAction(
                            AddTransactionAction.IncomeFormAction(
                                TransactionFormAction.MoneyChanged(
                                    it
                                )
                            )
                        )
                    },
                    onCategoryChange = {
                        onAction(
                            AddTransactionAction.IncomeFormAction(
                                TransactionFormAction.CategoryChanged(
                                    it
                                )
                            )
                        )
                    },
                    onTransactionDateChange = {
                        onAction(
                            AddTransactionAction.IncomeFormAction(
                                TransactionFormAction.DateChanged(
                                    it
                                )
                            )
                        )
                    },
                    onNoteChange = {
                        onAction(
                            AddTransactionAction.IncomeFormAction(
                                TransactionFormAction.NoteChanged(
                                    it
                                )
                            )
                        )
                    },
                    onNewCategoryCreated = {
                        onAction(
                            AddTransactionAction.IncomeFormAction(
                                TransactionFormAction.CategoryChanged(
                                    it
                                )
                            )
                        )
                    },
                    onSaveClick = {
                        onAction(AddTransactionAction.IncomeFormAction(TransactionFormAction.SaveClicked))

                    },
                    onNameChange = {
                        onAction(
                            AddTransactionAction.IncomeFormAction(
                                TransactionFormAction.NameChanged(
                                    it
                                )
                            )
                        )
                    },

                    )
            }

        )

        val addExpenseScreen = Screen(
            title = UiText.StringResourceId(Res.string.add_expense).asString(), content = {
                AddTransactionForm(
                    state = state.expenseForm,
                    isExpense = true,
                    isCardPayment = state.expenseForm.isCardPayment,
                    onNameChange = {
                        onAction(
                            AddTransactionAction.ExpenseFormAction(
                                TransactionFormAction.NameChanged(
                                    it
                                )
                            )
                        )
                    },
                    onAmountChange = {
                        onAction(
                            AddTransactionAction.ExpenseFormAction(
                                TransactionFormAction.MoneyChanged(
                                    it
                                )
                            )
                        )
                    },

                    onCategoryChange = {
                        onAction(
                            AddTransactionAction.ExpenseFormAction(
                                TransactionFormAction.CategoryChanged(
                                    it
                                )
                            )
                        )
                    },
                    onTransactionDateChange = {
                        onAction(
                            AddTransactionAction.ExpenseFormAction(
                                TransactionFormAction.DateChanged(
                                    it
                                )
                            )
                        )
                    },
                    onNoteChange = {
                        onAction(
                            AddTransactionAction.ExpenseFormAction(
                                TransactionFormAction.NoteChanged(
                                    it
                                )
                            )
                        )
                    },
                    onCardSelected = {
                        onAction(
                            AddTransactionAction.ExpenseFormAction(
                                TransactionFormAction.CardSelected(
                                    it
                                )
                            )
                        )
                    },
                    onInstallmentCountChange = {
                        onAction(
                            AddTransactionAction.ExpenseFormAction(
                                TransactionFormAction.InstallmentCountChanged(
                                    it
                                )
                            )
                        )
                    },
                    onInstallmentStartDateChange = {
                        onAction(
                            AddTransactionAction.ExpenseFormAction(
                                TransactionFormAction.InstallmentStartDateChanged(
                                    it
                                )
                            )
                        )
                    },
                    onNewCategoryCreated = {
                        onAction(
                            AddTransactionAction.ExpenseFormAction(
                                TransactionFormAction.CategoryChanged(
                                    it
                                )
                            )
                        )
                    },

                    onAddNewCardClicked = {
                        onAction(AddTransactionAction.ExpenseFormAction(TransactionFormAction.AddNewCardClicked))
                    },
                    onIsCardPaymentChanged = {
                        onAction(
                            AddTransactionAction.ExpenseFormAction(
                                TransactionFormAction.IsCardPaymentChanged(
                                    it
                                )
                            )
                        )
                    },
                    onSaveClick = {
                        onAction(AddTransactionAction.ExpenseFormAction(TransactionFormAction.SaveClicked))
                    })
            })

        TabbedScreen(
            initialPage = if (isIncomeByDefault) 1 else 0, onTabChanged = {}, screens = listOf(
                addExpenseScreen, addIncomeScreen
            )
        )

    }
}


