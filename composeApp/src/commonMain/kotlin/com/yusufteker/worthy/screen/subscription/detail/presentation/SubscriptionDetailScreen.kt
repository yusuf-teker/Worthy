package com.yusufteker.worthy.screen.subscription.detail.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.app.navigation.NavigationHandler
import com.yusufteker.worthy.app.navigation.NavigationModel
import com.yusufteker.worthy.core.domain.createTimestampId
import com.yusufteker.worthy.core.domain.getCurrentAppDate
import com.yusufteker.worthy.core.domain.getCurrentMonth
import com.yusufteker.worthy.core.domain.getCurrentYear
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.RecurringItem


import com.yusufteker.worthy.core.domain.model.format
import com.yusufteker.worthy.core.domain.model.getLastMonth
import com.yusufteker.worthy.core.domain.model.isActive
import com.yusufteker.worthy.core.domain.model.toEpochMillis
import com.yusufteker.worthy.core.domain.model.toMonthlyData
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.AppScaffold
import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
import com.yusufteker.worthy.core.presentation.components.AppButton
import com.yusufteker.worthy.core.presentation.components.AppTopBar
import com.yusufteker.worthy.core.presentation.components.MoneyInput
import com.yusufteker.worthy.core.presentation.components.SwipeToDeleteWrapper
import com.yusufteker.worthy.core.presentation.components.WheelDatePickerV2
import com.yusufteker.worthy.core.presentation.components.WheelDatePickerV3
import com.yusufteker.worthy.core.presentation.getMonthShortName
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppColors.primaryButtonColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.core.presentation.util.emptyMoney
import com.yusufteker.worthy.core.presentation.util.formatted
import com.yusufteker.worthy.screen.subscription.add.presentation.components.toComposeColor
import com.yusufteker.worthy.screen.subscriptiondetail.presentation.SubscriptionDetailAction
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.activate
import worthy.composeapp.generated.resources.amount_must_be_greater_than_zero
import worthy.composeapp.generated.resources.cancel
import worthy.composeapp.generated.resources.card_id
import worthy.composeapp.generated.resources.confirm
import worthy.composeapp.generated.resources.date_ranges_conflict
import worthy.composeapp.generated.resources.edit
import worthy.composeapp.generated.resources.end_date
import worthy.composeapp.generated.resources.end_date_required_for_past_start
import worthy.composeapp.generated.resources.missing_month_or_year
import worthy.composeapp.generated.resources.monthly_price
import worthy.composeapp.generated.resources.new_amount
import worthy.composeapp.generated.resources.payment_day
import worthy.composeapp.generated.resources.pick_end_date
import worthy.composeapp.generated.resources.pick_start_date
import worthy.composeapp.generated.resources.start_date
import worthy.composeapp.generated.resources.start_date_after_end_date
import worthy.composeapp.generated.resources.start_date_must_be_before
import worthy.composeapp.generated.resources.start_dates_cannot_be_same
import worthy.composeapp.generated.resources.streak_text
import worthy.composeapp.generated.resources.terminate
import worthy.composeapp.generated.resources.update
import kotlin.time.ExperimentalTime

@Composable
fun SubscriptionDetailScreenRoot(
    subscriptionId: Int,
    viewModel: SubscriptionDetailViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    onNavigateTo: (NavigationModel) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NavigationHandler(viewModel) { model ->
        onNavigateTo(model)
    }

    viewModel.onAction(SubscriptionDetailAction.Init(subscriptionId))

    BaseContentWrapper(state = state) {
        SubscriptionDetailScreen(
            modifier = it,
            state = state,
            onAction = viewModel::onAction,
            contentPadding = contentPadding
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionDetailScreen(
    modifier: Modifier = Modifier,
    state: SubscriptionDetailState,
    onAction: (action: SubscriptionDetailAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    var showTerminateActivateBottomSheet by remember { mutableStateOf(false) }
    var showHistoryEditor by remember { mutableStateOf(false) }


    AppScaffold(modifier = modifier.padding(contentPadding).clickable {
        showTerminateActivateBottomSheet = false
        showHistoryEditor = false
    }, topBar = {
        AppTopBar(
            title = state.subscription?.name ?: "",
            onNavIconClick = { onAction(SubscriptionDetailAction.NavigateBack) })
    }) { innerPadding ->
        state.subscription?.let { subscription ->
            Column(
                modifier = modifier.padding(innerPadding).fillMaxSize().padding(16.dp),

                ) {
                // Header Card: Icon + Name + Category
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = AppColors.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(64.dp).background(
                                subscription.colorHex?.toComposeColor() ?: AppColors.primary,
                                CircleShape
                            ), contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = subscription.icon,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                        Column {
                            Text(
                                subscription.name,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            subscription.category?.let {
                                Text(
                                    it.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                            }
                        }

                    }
                }

                Spacer(Modifier.height(20.dp))
                // Price Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = AppColors.secondaryContainer),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            UiText.StringResourceId(Res.string.monthly_price).asString(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                        Text(
                            text = subscription.amount.formatted(),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.primary
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = AppColors.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        state.activeStreak?.let {
                            SubscriptionStreakBadge(it, Modifier.align(Alignment.End))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                UiText.StringResourceId(Res.string.start_date).asString(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    state.subscription.startDate.format(showDay = false),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        state.subscription.endDate?.let {
                            DetailRow(
                                UiText.StringResourceId(Res.string.end_date).asString(),
                                it.format(showDay = false)
                            )
                        }

                        state.subscription.scheduledDay?.let {
                            DetailRow(
                                UiText.StringResourceId(Res.string.payment_day).asString(), "$it"
                            )
                        }

                        state.subscription.cardId?.let {
                            DetailRow(UiText.StringResourceId(Res.string.card_id).asString(), "$it")
                        }

                    }
                }
                Spacer(Modifier.height(20.dp))

                MiniSubscriptionChart(state.subscriptions.toMonthlyData())

                Spacer(Modifier.weight(1f))
                AppButton( // EDIT
                    text = UiText.StringResourceId(Res.string.edit).asString(), onClick = {
                        showHistoryEditor = true
                    }, trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Edit, contentDescription = "Edit history"
                        )

                    }, loading = state.isLoading, modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                AppButton( // ACTIVATION
                    text = if (subscription.isActive()) UiText.StringResourceId(Res.string.terminate)
                        .asString() else UiText.StringResourceId(Res.string.activate).asString(),
                    onClick = {
                        showTerminateActivateBottomSheet = true
                    },
                    loading = state.isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    colors = primaryButtonColors.copy(
                        containerColor = if (state.subscription.isActive()) Color.Red else Color.Green,
                    )
                )

            }
        }

        // Bottom Sheet for Date Selection
        if (showTerminateActivateBottomSheet && state.subscription != null) {
            ActivationEditor(
                isActive = state.subscription.isActive(),
                pickedDate = state.pickedDate,
                onDateSelected = { onAction(SubscriptionDetailAction.OnDateSelected(it)) },
                onConfirm = {
                    if (state.subscription.isActive()) {
                        onAction(SubscriptionDetailAction.EndSubscription(state.pickedDate))
                    } else {
                        onAction(SubscriptionDetailAction.ActivateSubscription(state.pickedDate))
                    }
                    showTerminateActivateBottomSheet = false
                },
                onDismiss = { showTerminateActivateBottomSheet = false }

            )
        }

        // History Editor Bottom Sheet
        if (showHistoryEditor && state.subscription != null) {
            SubscriptionHistoryEditor(
                history = state.subscriptions,
                onDismiss = { showHistoryEditor = false },
                onSave = { items ->
                    onAction(SubscriptionDetailAction.OnUpdateRecurringItems(items))
                }

            )
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(label, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivationEditor(
    isActive: Boolean,
    pickedDate: AppDate,
    onDateSelected: (AppDate) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss, sheetState = rememberModalBottomSheetState()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                if (isActive) UiText.StringResourceId(Res.string.pick_end_date).asString()
                else UiText.StringResourceId(Res.string.pick_start_date).asString(),
                style = MaterialTheme.typography.titleMedium
            )

            WheelDatePickerV3(
                initialDate = pickedDate,
                onDateSelected = { date -> onDateSelected(date) },
                showDay = false
            )

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text(UiText.StringResourceId(Res.string.cancel).asString())
                }
                Button(onClick = onConfirm) {
                    Text(UiText.StringResourceId(Res.string.confirm).asString())
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionHistoryEditor(
    history: List<RecurringItem.Subscription>,
    onDismiss: () -> Unit,
    onSave: (List<RecurringItem.Subscription>) -> Unit
) {
    var items by remember { mutableStateOf(history.sortedByDescending { it.startDate }) }
    var newAmount by remember { mutableStateOf<Money?>(emptyMoney()) }
    var newStartDate by remember { mutableStateOf<AppDate?>(getCurrentAppDate()) }
    var newEndDate by remember { mutableStateOf<AppDate?>(null) }
    var name by remember { mutableStateOf(items.firstOrNull()?.name ?: "") }

    var validationErrors by remember { mutableStateOf<Pair<Int, UiText>?>(null) }
    val listState = rememberLazyListState()

    LaunchedEffect(validationErrors) {
        validationErrors?.let { (id, _) ->
            val index = items.indexOfFirst { it.id == id }
            if (index >= 0) {
                listState.animateScrollToItem(index + 2)
                // +2 çünkü ilk item "NewSubscription", ikincisi divider başlık
            }
        }
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(16.dp)) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f, fill = false),
                state = listState
            ) {
                // 🔹 Header - Yeni Abonelik Ekleme Bölümü
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                            containerColor = AppColors.primary.copy(alpha = 0.1f)
                        ), border = BorderStroke(2.dp, AppColors.primary.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add new",
                                    tint = AppColors.primary
                                )
                                Text(
                                    "Update Current Subscription",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = AppColors.primary
                                )
                            }

                            var errorMessageNew by remember { mutableStateOf<UiText?>(null) }

                            NewSubscription(
                                amount = newAmount,
                                errorMessage = errorMessageNew?.asString(),
                                onAmountChange = {
                                    newAmount = it
                                    errorMessageNew = null
                                },
                                starDate = newStartDate ?: getCurrentAppDate(),
                                onStarDateChange = {
                                    newStartDate = it
                                    errorMessageNew = null
                                },
                                onSave = {
                                    val newItem = RecurringItem.Subscription(
                                        id = 0,
                                        name = name,
                                        amount = Money(
                                            amount = newAmount?.amount ?: 0.0,
                                            currency = newAmount?.currency ?: Currency.TRY
                                        ),
                                        endDate = newEndDate,
                                        groupId = items.firstOrNull()?.groupId
                                            ?: createTimestampId(),
                                        startDate = newStartDate ?: getCurrentAppDate()
                                    )

                                    val tempItems = items.toMutableList()
                                    tempItems.add(newItem)
                                    errorMessageNew = hasDateConflict(tempItems, newItem)?.second
                                    if (errorMessageNew == null) {
                                        onSave(adjustOpenEndedRecurringItems(tempItems))
                                    }
                                    onDismiss.invoke()
                                })
                        }
                    }
                }

                // 🔹 Divider ve Geçmiş Başlığı
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        HorizontalDivider(
                            thickness = 2.dp, color = AppColors.outline.copy(alpha = 0.3f)
                        )
                        Text(
                            "Subscription History",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppColors.onSurface.copy(alpha = 0.7f),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }

                // 🔹 Mevcut Abonelikler (Geçmişten Günümüze)
                itemsIndexed(items) { index, sub ->
                    val isCurrentSubscription = index == 0

                    SwipeToDeleteWrapper(
                        onDelete = {
                            items = items.filterIndexed { i, _ -> i != index }
                        }) {
                        ExistingSubscription(
                            subscription = sub, onUpdate = { newItem ->
                                items = items.map {
                                    if (it.id == newItem.id) newItem
                                    else it
                                }
                            }, validationError = if (validationErrors?.first == sub.id) {
                                validationErrors?.second?.asString()
                            } else null, isLast = isCurrentSubscription
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Save butonu
            Button(
                modifier = Modifier.fillMaxWidth(), onClick = {
                    validationErrors = hasDateConflict(items)
                    if (validationErrors != null) {
                        return@Button
                    } else {
                        onSave(items.sortedBy { it.startDate })
                        onDismiss()
                    }
                }) {
                Text("Save Changes")
            }
        }
    }
}

@Composable
fun NewSubscription(
    amount: Money?,
    onAmountChange: (Money?) -> Unit,
    starDate: AppDate,
    onStarDateChange: (AppDate) -> Unit,
    onSave: () -> Unit,
    errorMessage: String? = null
) {
    var amountError by remember { mutableStateOf<UiText?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MoneyInput(
            label = UiText.StringResourceId(Res.string.new_amount),
            money = amount ?: Money(amount = 0.0, currency = Currency.TRY),
            onValueChange = { newAmount ->
                onAmountChange(newAmount)
                amountError = null
            },
            modifier = Modifier.fillMaxWidth(),
            isError = amountError != null,
            errorMessage = amountError
        )

        WheelDatePickerV2(
            initialDate = starDate, onDateSelected = { date ->
                onStarDateChange(date)
            }, showDay = false
        )

        if (!errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = AppTypography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Button(
            onClick = {
                if ((amount?.amount ?: 0.0) <= 0.0) {
                    amountError =
                        UiText.StringResourceId(Res.string.amount_must_be_greater_than_zero)
                } else {
                    onSave()
                }
            }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.primary
            )
        ) {
            Text(
                UiText.StringResourceId(Res.string.update).asString(),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ExistingSubscription(
    subscription: RecurringItem.Subscription,
    onUpdate: (RecurringItem.Subscription) -> Unit,
    validationError: String?,
    isLast: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors().copy(
            containerColor = AppColors.secondaryContainer
        ), border = if (isLast) BorderStroke(1.dp, AppColors.secondary.copy(alpha = 0.5f))
        else null
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (isLast) "Current Period" else "Previous Period",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isLast) AppColors.secondary else AppColors.onSurface.copy(alpha = 0.7f)
                )

                if (isLast) {
                    Surface(
                        color = AppColors.secondary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "ACTIVE",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.secondary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            WheelDatePickerV3(
                title = "Start Date:",
                selectedMonth = subscription.startDate.month,
                selectedYear = subscription.startDate.year,
                selectedDay = 1,
                onDateSelected = { date ->
                    onUpdate(subscription.copy(startDate = date))
                },
                showDay = false,
                backgroundColor = AppColors.secondaryContainer
            )

            var hasEndDate by remember { mutableStateOf(subscription.endDate != null) }

            if (isLast) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "End Date (Optional):",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Checkbox(
                        checked = hasEndDate, onCheckedChange = { isChecked ->
                            hasEndDate = isChecked
                            if (!isChecked) {
                                onUpdate(subscription.copy(endDate = null))
                            }
                        })
                    Text("Has end date", style = MaterialTheme.typography.bodySmall)
                }
            }

            if (subscription.endDate != null && !isLast) {
                WheelDatePickerV3(
                    title = UiText.StringResourceId(Res.string.end_date).asString(),
                    selectedDay = 1,
                    selectedYear = subscription.endDate.year,
                    selectedMonth = subscription.endDate.month,
                    onDateSelected = { date ->
                        onUpdate(subscription.copy(endDate = date))
                    },
                    showDay = false,
                    backgroundColor = AppColors.secondaryContainer
                )
            } else if (isLast && hasEndDate) {
                WheelDatePickerV3(
                    title = UiText.StringResourceId(Res.string.end_date).asString(),
                    selectedDay = 1,
                    selectedYear = subscription.endDate?.year ?: getCurrentYear(),
                    selectedMonth = subscription.endDate?.month ?: getCurrentMonth(),
                    onDateSelected = { date ->
                        onUpdate(subscription.copy(endDate = date))
                    },
                    showDay = false,
                    backgroundColor = AppColors.secondaryContainer
                )
            }

            MoneyInput(
                label = UiText.StringResourceId(Res.string.monthly_price),
                money = subscription.amount,
                onValueChange = { amount ->
                    onUpdate(subscription.copy(amount = amount ?: emptyMoney()))
                })

            validationError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
fun hasDateConflict(
    items: List<RecurringItem.Subscription>, newItem: RecurringItem.Subscription? = null
): Pair<Int, UiText>? {
    val sorted = items.sortedBy { it.startDate }
 //   val sortedMaxEnd = items.sortedBy { it.endDate }
    val lastUpdatedBeforeNew = items.find { it.endDate == null && it.id != newItem?.id }
    lastUpdatedBeforeNew?.let {
        newItem?.let { item ->
            if (it.startDate >= item.startDate) {
                return Pair(
                    it.id, UiText.StringResourceId(
                        id = Res.string.end_date_required_for_past_start,
                    )
                )
            }

        }
    }
    for (i in 0 until sorted.size - 1) {
        val current = sorted[i]
        val next = sorted[i + 1]

        val currentStart = current.startDate
        val currentEnd = current.endDate ?: current.startDate
        val nextStart = next.startDate

        // 1. Aynı startDate olamaz
        if (currentStart == nextStart) {

            return Pair(
                current.id, UiText.StringResourceId(
                    id = Res.string.start_dates_cannot_be_same,
                    args = arrayOf(currentStart.month, currentStart.year)
                )
            )

        }

        // 2. Yeni başlangıç tarihi, eskiye göre max 1 ay sonra olabilir
        val today = getCurrentAppDate()
        val latestAllowed = today.nextMonth()
        if (currentStart > latestAllowed) {
            return Pair(
                current.id, UiText.StringResourceId(
                    id = Res.string.start_date_must_be_before,
                    args = arrayOf(latestAllowed.month, latestAllowed.year)
                )

            )

        }
        // 3. Sonraki item'ın başlangıç tarihi, mevcut item'ın tarih aralığının içine düşemez
        if (nextStart > currentStart && nextStart < currentEnd) {
            return Pair(
                next.id, UiText.StringResourceId(
                    id = Res.string.date_ranges_conflict, args = arrayOf(
                        next.name,
                        nextStart.month,
                        nextStart.year,
                        current.name,
                        currentStart.month,
                        currentStart.year,
                        currentEnd.month,
                        currentEnd.year
                    )
                )
            )
        }

        // 3. current'ın endDate'i, sonraki startDate'den önce olmalı (== de dahil değil)
        if (currentEnd >= nextStart) return Pair(
            current.id, UiText.StringResourceId(
                id = Res.string.date_ranges_conflict, args = arrayOf(
                    current.name,
                    currentStart.month,
                    currentStart.year,
                    next.name,
                    nextStart.month,
                    nextStart.year
                )
            )
        )

    }
    for (item in sorted) {
        val start = item.startDate
        val end = item.endDate

        if (end != null && start > end) {
            return Pair(
                item.id, UiText.StringResourceId(
                    id = Res.string.start_date_after_end_date, args = arrayOf(
                        item.name, start.month, start.year, end.month, end.year
                    )
                )
            )
        }
        if (item.endDate?.month == null && item.endDate?.year != null) {
            return Pair(
                item.id, UiText.StringResourceId(
                    id = Res.string.missing_month_or_year
                )
            )
        }

    }

    return null
}

fun adjustOpenEndedRecurringItems(items: List<RecurringItem.Subscription>): List<RecurringItem.Subscription> {
    val sorted = items.sortedBy { it.startDate }

    return sorted.mapIndexed { index, current ->
        if (current.endDate == null && index < sorted.lastIndex) {
            val next = sorted[index + 1]
            val adjustedEndDate = next.startDate.getLastMonth()
            current.copy(
                endDate = AppDate(
                    month = adjustedEndDate.month,
                    year = adjustedEndDate.year,
                    day = current.endDate?.day
                ),
            )
        } else {
            current
        }
    }
}

@Composable
fun SubscriptionStreakBadge(
    activeMonths: Int, modifier: Modifier = Modifier
) {
    if (activeMonths > 0) {
        Surface(
            color = AppColors.primary.copy(alpha = 0.1f),
            shape = RoundedCornerShape(50),
            modifier = modifier.padding(top = 8.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = UiText.StringResourceId(Res.string.streak_text, arrayOf(activeMonths))
                        .asString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.primary
                )
            }
        }
    }
}

@Composable
fun MiniSubscriptionChart(
    data: List<Pair<AppDate, Money>>,
    modifier: Modifier = Modifier,
    lineColor: Color = AppColors.primary,
    height: Dp = 120.dp
) {
    if (data.isEmpty()) return

    val sortedData = data.sortedBy { it.first.toEpochMillis() }
    val minY = sortedData.minOf { it.second.amount }
    val maxY = sortedData.maxOf { it.second.amount }

    Card(

        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = AppColors.secondaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = modifier.padding(start = 8.dp, end = 16.dp, top = 16.dp)) {
            Box(modifier = Modifier.height(height).fillMaxWidth()) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val widthPx = size.width
                    val heightPx = size.height
                    val paddingX = 40f
                    val paddingY = 20f

                    val chartWidth = widthPx - paddingX
                    val chartHeight = heightPx - paddingY

                    fun mapX(index: Int): Float {
                        return paddingX + (chartWidth / (sortedData.size - 1).coerceAtLeast(1)) * index
                    }

                    fun mapY(amount: Double): Float {
                        return if (maxY == minY) { //eşit olunca sıkıntı oluyordu
                            heightPx / 2f // tüm noktalar aynı yatay çizgi üzerinde
                        } else {
                            heightPx - paddingY - ((amount - minY) / (maxY - minY) * chartHeight).toFloat()
                        }
                    }

                    // Kesintisiz çizgiler çiz
                    var currentPath = Path()
                    var pathStarted = false

                    sortedData.forEachIndexed { index, (date, money) ->
                        val x = mapX(index)
                        val y = mapY(money.amount)

                        // Önceki nokta ile bu nokta arasında kesinti var mı kontrol et
                        val shouldBreakLine = if (index > 0) {
                            val previousDate = sortedData[index - 1].first
                            !isConsecutiveMonth(previousDate, date)
                        } else false

                        if (shouldBreakLine && pathStarted) {
                            // Önceki path'i çiz ve yeni path başlat
                            drawPath(
                                currentPath,
                                color = lineColor,
                                style = Stroke(width = 4f, cap = StrokeCap.Round)
                            )
                            currentPath = Path()
                            pathStarted = false
                        }

                        // Path'e nokta ekle
                        if (!pathStarted) {
                            currentPath.moveTo(x, y)
                            pathStarted = true
                        } else {
                            currentPath.lineTo(x, y)
                        }
                    }

                    // Son path'i çiz
                    if (pathStarted) {
                        drawPath(
                            currentPath,
                            color = lineColor,
                            style = Stroke(width = 4f, cap = StrokeCap.Round)
                        )
                    }

                    // Noktalar (tüm noktaları çiz, çizgi olsun olmasın)
                    sortedData.forEachIndexed { index, (_, money) ->
                        val x = mapX(index)
                        val y = mapY(money.amount)
                        drawCircle(
                            color = lineColor,
                            radius = 4f,
                            center = androidx.compose.ui.geometry.Offset(x, y)
                        )
                    }
                }
            }

            // X ekseni etiketleri
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                sortedData.forEach { (date, _) ->
                    Text(
                        getMonthShortName(date.month),
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )
                }
            }
        }
    }

}

/**
 * İki AppDate'in ardışık aylar olup olmadığını kontrol eder
 */
private fun isConsecutiveMonth(date1: AppDate, date2: AppDate): Boolean {
    val nextMonth = date1.nextMonth()
    return nextMonth.year == date2.year && nextMonth.month == date2.month
}





