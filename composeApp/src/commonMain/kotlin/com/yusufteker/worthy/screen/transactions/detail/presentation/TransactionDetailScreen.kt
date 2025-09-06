package com.yusufteker.worthy.screen.transactions.detail.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.app.navigation.NavigationHandler
import com.yusufteker.worthy.app.navigation.NavigationModel
import com.yusufteker.worthy.core.domain.model.CategoryType
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.domain.model.emptyMoney
import com.yusufteker.worthy.core.domain.model.toAppDate
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
import com.yusufteker.worthy.core.presentation.components.AppButton
import com.yusufteker.worthy.core.presentation.components.CategorySelector
import com.yusufteker.worthy.core.presentation.components.MoneyInput
import com.yusufteker.worthy.core.presentation.components.WheelDatePicker
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.add
import worthy.composeapp.generated.resources.note
import worthy.composeapp.generated.resources.wishlist_label_product_name
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
fun TransactionDetailScreenRoot(
    transactionId: Int,
    viewModel: TransactionDetailViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    onNavigateTo: (NavigationModel) -> Unit,

    ) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.onAction(TransactionDetailAction.Init(transactionId))
    NavigationHandler(viewModel) { model ->
        onNavigateTo(model)
    }

    BaseContentWrapper(state = state) { modifier ->
        TransactionDetailScreen(
            state = state,
            onAction = viewModel::onAction,
            contentPadding = contentPadding,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun TransactionDetailScreen(
    state: TransactionDetailState,
    onAction: (action: TransactionDetailAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
    modifier: Modifier = Modifier

) {

    Scaffold(
        modifier = modifier.fillMaxSize().padding(contentPadding),
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            var name by remember { mutableStateOf(state.transaction?.name) }
            var errorName by remember { mutableStateOf<String?>(null) }
            var errorMoney by remember { mutableStateOf<UiText?>(null) }
            var errorCategory by remember { mutableStateOf<UiText?>(null) }
            var errorDate by remember { mutableStateOf<UiText?>(null) }

            var amount by remember { mutableStateOf(state.transaction?.amount) }
            var note by remember { mutableStateOf(state.transaction?.note ?: "") }
            var transactionType by remember { mutableStateOf(state.transaction?.transactionType) }
            var date = remember { state.transaction?.transactionDate?.toAppDate() }

            Column(
                modifier = modifier.fillMaxSize().padding(top = paddingValues.calculateTopPadding())
                    .verticalScroll(rememberScrollState())
            ) {
                state.transaction?.let {

                    // --- Name ---
                    OutlinedTextField(
                        value = name ?: "", onValueChange = {
                        name = it
                    }, label = {
                        Text(
                            UiText.StringResourceId(Res.string.wishlist_label_product_name)
                                .asString()
                        )
                    }, modifier = Modifier.fillMaxWidth(), isError = errorName != null
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- Amount ---
                    MoneyInput(
                        money = amount, onValueChange = {

                            amount = it
                        }, errorMessage = errorMoney, isError = errorMoney != null
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- Category ---
                    CategorySelector(
                        categories = state.categories,
                        selectedCategory = state.selectedCategory,
                        onCategorySelected = {
                            onAction(TransactionDetailAction.UpdateCategory(it))
                        },
                        onNewCategoryCreated = {
                            onAction(TransactionDetailAction.CreateCategory(it))
                        },
                        categoryType = if (state.transaction.transactionType == TransactionType.EXPENSE) CategoryType.EXPENSE else CategoryType.INCOME,
                        errorMessage = errorCategory
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- Date ---
                    val currentDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

                    WheelDatePicker(
                        initialDate = currentDate,
                        onDateSelected = { epochSeconds ->
                            date = epochSeconds.toAppDate()
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        title = "Date Added", // todo tr en
                        errorMessage = errorDate?.asString()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // --- Note ---

                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text(UiText.StringResourceId(Res.string.note).asString()) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    // --- Save Button ---

                    AppButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = UiText.StringResourceId(Res.string.add).asString(),
                        loading = state.isLoading,
                        onClick = {
                            val updatedTransaction = it.copy(
                                name = name ?: "",
                                amount = amount ?: emptyMoney(),
                                note = note,
                                categoryId = state.selectedCategory?.id ?: 0,
                            )
                            onAction(TransactionDetailAction.UpdateTransaction(updatedTransaction))

                        },
                    )

                }

            }
        }
    }

}