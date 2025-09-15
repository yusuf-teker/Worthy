package com.yusufteker.worthy.screen.transactions.detail.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.app.navigation.NavigationHandler
import com.yusufteker.worthy.app.navigation.NavigationModel
import com.yusufteker.worthy.core.domain.model.CategoryType
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.domain.model.toAppDate
import com.yusufteker.worthy.core.domain.model.toEpochMillis
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.AppScaffold
import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
import com.yusufteker.worthy.core.presentation.components.AppButton
import com.yusufteker.worthy.core.presentation.components.AppTopBar
import com.yusufteker.worthy.core.presentation.components.CardSelector
import com.yusufteker.worthy.core.presentation.components.CategorySelector
import com.yusufteker.worthy.core.presentation.components.MoneyInput
import com.yusufteker.worthy.core.presentation.components.WheelDatePickerV3
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppColors.primaryButtonColors
import com.yusufteker.worthy.screen.subscriptiondetail.presentation.SubscriptionDetailAction
import io.github.aakira.napier.Napier
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.date_added
import worthy.composeapp.generated.resources.delete
import worthy.composeapp.generated.resources.note
import worthy.composeapp.generated.resources.refund_button
import worthy.composeapp.generated.resources.screen_title_transaction_detail
import worthy.composeapp.generated.resources.summary_refund
import worthy.composeapp.generated.resources.update
import worthy.composeapp.generated.resources.wishlist_label_product_name
import kotlin.time.ExperimentalTime

@Composable
fun TransactionDetailScreenRoot(
    transactionId: Int,
    viewModel: TransactionDetailViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    onNavigateTo: (NavigationModel) -> Unit,

    ) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = transactionId) {
        viewModel.onAction(TransactionDetailAction.Init(transactionId))
    }
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

    LaunchedEffect(key1 = state.isLoading) {
        Napier.d("TransactionDetailScreen: isLoading = ${state.isLoading}")
    }

    AppScaffold(
        modifier = modifier.fillMaxSize().padding(contentPadding), topBar = {
            AppTopBar(
                title = UiText.StringResourceId(Res.string.screen_title_transaction_detail)
                    .asString(),
                modifier = Modifier.fillMaxWidth(),
                onNavIconClick = { onAction(TransactionDetailAction.NavigateBack) },
                showDivider = true,
                actions = {
                    if (state.transaction != null) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Transaction",
                            modifier = Modifier.size(28.dp).clickable {
                                onAction(
                                    TransactionDetailAction.DeleteTransaction(
                                        state.transaction
                                    )
                                )
                            }.padding(2.dp),
                            tint = AppColors.icon_red
                        )
                    }
                }
            )
        }) { paddingValues ->
        Column(
            modifier = modifier.padding(paddingValues).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            var errorName by remember { mutableStateOf<String?>(null) }
            var errorMoney by remember { mutableStateOf<UiText?>(null) }
            var errorCategory by remember { mutableStateOf<UiText?>(null) }
            var errorDate by remember { mutableStateOf<UiText?>(null) }

            Column(
                modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState())
            ) {
                state.transaction?.let {

                    // --- Name ---
                    OutlinedTextField(

                        value = it.name, onValueChange = {
                            onAction(TransactionDetailAction.UpdateName(it))
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
                        money = it.amount, onValueChange = { newMoney ->
                            onAction(TransactionDetailAction.UpdateAmount(newMoney))
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

                    WheelDatePickerV3(
                        initialDate = state.transaction.transactionDate.toAppDate(),
                        onDateSelected = { epochSeconds ->
                            onAction(TransactionDetailAction.UpdateDate(epochSeconds.toEpochMillis()))
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        title = UiText.StringResourceId(Res.string.date_added).asString(),
                        errorMessage = errorDate?.asString()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // --- Note ---

                    OutlinedTextField(
                        value = it.note ?: "",
                        onValueChange = { note -> onAction(TransactionDetailAction.UpdateNote(note)) },
                        label = { Text(UiText.StringResourceId(Res.string.note).asString()) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    CardSelector(
                        cards = state.cards,
                        selectedCard = it.cardId?.let { id -> state.cards.find { card -> card.id == id } },
                        onCardSelected = { selectedCard ->
                            onAction(TransactionDetailAction.UpdateCard(selectedCard.id))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        onAddNewCard = {
                            onAction(TransactionDetailAction.NavigateToAddCardScreen)
                        },
                        //errorMessage = state.errorCard // todo ekle
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    // --- Save Button ---

                    Row {
                        AppButton(
                            modifier = Modifier.weight(1f),
                            text = UiText.StringResourceId(Res.string.update).asString(),
                            loading = state.isLoading,
                            onClick = {
                                onAction(TransactionDetailAction.UpdateTransaction(it))
                            },
                        )


                        if (state.isRefund != null && !state.isRefund){ // REFUND DEĞİL ise refund butonu
                            Spacer(Modifier.width(16.dp))
                            AppButton(
                                modifier = Modifier.weight(1f),
                                text = UiText.StringResourceId(Res.string.refund_button).asString(),
                                loading = state.isLoading,
                                onClick = {
                                    onAction(TransactionDetailAction.RefundTransaction(it))
                                },
                                colors = primaryButtonColors.copy(
                                    containerColor = Color.Red,
                                )
                            )
                        }

                    }


                }

            }
        }
    }

}