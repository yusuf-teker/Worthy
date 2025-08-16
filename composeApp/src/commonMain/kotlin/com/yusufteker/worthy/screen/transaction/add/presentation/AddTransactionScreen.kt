package com.yusufteker.worthy.screen.addtransaction.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.core.domain.model.Card
import com.yusufteker.worthy.core.presentation.components.MoneyInput
import com.yusufteker.worthy.core.presentation.components.Screen
import com.yusufteker.worthy.core.presentation.components.TabbedScreen
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.screen.transaction.add.presentation.components.AddTransactionForm
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddTransactionScreenRoot(
    viewModel: AddTransactionViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    isIncomeByDefault: Boolean = false
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AddTransactionScreen(
        state = state,
        onAction = viewModel::onAction,
        contentPadding = contentPadding,
        isIncomeByDefault = isIncomeByDefault
    )
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
        // TODO
        val addIncomeScreen = Screen(
            title = "Gelir Ekle",
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
                    onCardSelected = {
                        onAction(AddTransactionAction.IncomeFormAction(TransactionFormAction.CardSelected(it)))
                    },
                    onNewCategoryCreated = {
                        onAction(AddTransactionAction.IncomeFormAction(TransactionFormAction.CategoryChanged(it)))
                    },
                    onSaveClick = {
                        onAction(AddTransactionAction.IncomeFormAction(TransactionFormAction.SaveClicked))

                    },
                    onNameChange = {
                        onAction(AddTransactionAction.IncomeFormAction(TransactionFormAction.NameChanged(it)))
                    }


                )
            }

        )

        val addExpenseScreen = Screen(
            title = "Gider Ekle",
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


