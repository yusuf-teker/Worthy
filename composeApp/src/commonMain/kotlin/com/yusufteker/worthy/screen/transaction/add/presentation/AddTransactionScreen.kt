package com.yusufteker.worthy.screen.addtransaction.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.domain.model.Card
import com.yusufteker.worthy.core.presentation.UiEvent
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.components.AppTopBar
import com.yusufteker.worthy.core.presentation.components.MoneyInput
import com.yusufteker.worthy.core.presentation.components.Screen
import com.yusufteker.worthy.core.presentation.components.TabbedScreen
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.screen.transaction.add.presentation.components.AddTransactionForm
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.add_expense
import worthy.composeapp.generated.resources.add_income
import worthy.composeapp.generated.resources.add_new
import worthy.composeapp.generated.resources.screen_title_new_transaction

@Composable
fun AddTransactionScreenRoot(
    viewModel: AddTransactionViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    isIncomeByDefault: Boolean = false,
    navigateBack: () -> Unit = {},
    navigateToAddCardScreen: () -> Unit = {}

    ) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AddTransactionScreen(
        state = state,
        onAction = viewModel::onAction,
        contentPadding = contentPadding,
        isIncomeByDefault = isIncomeByDefault
    )

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when(event){
                is UiEvent.NavigateBack -> {
                    navigateBack.invoke()
                }
                is UiEvent.NavigateTo -> {
                    if (event.route == Routes.AddCard){
                        navigateToAddCardScreen.invoke()
                    }

                }
                else -> Unit
            }
        }
    }
}

@Composable
fun AddTransactionScreen(
    state: AddTransactionState,
    onAction: (action: AddTransactionAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
    isIncomeByDefault: Boolean = false

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        AppTopBar(
            title = UiText.StringResourceId(Res.string.screen_title_new_transaction).asString(),
            onNavIconClick = { onAction(AddTransactionAction.OnBackClick) },
            isBack = true
        )

        val addIncomeScreen = Screen(
            title = UiText.StringResourceId(Res.string.add_income).asString(),
            content = {
                AddTransactionForm(
                    state = state.incomeForm,
                    isExpense = false,
                    onAmountChange = {
                        onAction(AddTransactionAction.IncomeFormAction(TransactionFormAction.MoneyChanged(it)))
                    },
                    onCategoryChange = {
                        onAction(AddTransactionAction.IncomeFormAction(TransactionFormAction.CategoryChanged(it)))
                    },
                    onTransactionDateChange = {
                        onAction(AddTransactionAction.IncomeFormAction(TransactionFormAction.DateChanged(it)))
                    },
                    onNoteChange = {
                        onAction(AddTransactionAction.IncomeFormAction(TransactionFormAction.NoteChanged(it)))
                    },
                    onNewCategoryCreated = {
                        onAction(AddTransactionAction.IncomeFormAction(TransactionFormAction.CategoryChanged(it)))
                    },
                    onSaveClick = {
                        onAction(AddTransactionAction.IncomeFormAction(TransactionFormAction.SaveClicked))

                    },
                    onNameChange = {
                        onAction(AddTransactionAction.IncomeFormAction(TransactionFormAction.NameChanged(it)))
                    },

                )
            }

        )

        val addExpenseScreen = Screen(
            title = UiText.StringResourceId(Res.string.add_expense).asString(),
            content = {
                AddTransactionForm(
                    state = state.expenseForm,
                    isExpense = true,

                    onNameChange = {
                        onAction(AddTransactionAction.ExpenseFormAction(TransactionFormAction.NameChanged(it)))
                    },
                    onAmountChange = {
                        onAction(AddTransactionAction.ExpenseFormAction(TransactionFormAction.MoneyChanged(it)))
                    },

                    onCategoryChange = {
                        onAction(AddTransactionAction.ExpenseFormAction(TransactionFormAction.CategoryChanged(it)))
                    },
                    onTransactionDateChange = {
                        onAction(AddTransactionAction.ExpenseFormAction(TransactionFormAction.DateChanged(it)))
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
                        onAction(AddTransactionAction.ExpenseFormAction(TransactionFormAction.CardSelected(it)))
                    },
                    onInstallmentCountChange = {
                        onAction(AddTransactionAction.ExpenseFormAction(TransactionFormAction.InstallmentCountChanged(it)))
                    },
                    onInstallmentStartDateChange = {
                        onAction(AddTransactionAction.ExpenseFormAction(TransactionFormAction.InstallmentStartDateChanged(it)))
                    },
                    onNewCategoryCreated = {
                        onAction(AddTransactionAction.ExpenseFormAction(TransactionFormAction.CategoryChanged(it)))
                    },

                    onAddNewCardClicked = {
                        onAction(AddTransactionAction.IncomeFormAction(TransactionFormAction.AddNewCardClicked))
                    },
                    onSaveClick = {
                        onAction(AddTransactionAction.ExpenseFormAction(TransactionFormAction.SaveClicked))
                    }
                )
            }
        )

        TabbedScreen(
            initialPage = if (isIncomeByDefault) 1 else 0,
            onTabChanged = {},
            screens = listOf(
                addExpenseScreen,
                addIncomeScreen
            )
        )

    }
}


