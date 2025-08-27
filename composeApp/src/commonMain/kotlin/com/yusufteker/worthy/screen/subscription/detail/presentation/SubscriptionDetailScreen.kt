package com.yusufteker.worthy.screen.subscriptiondetail.presentation


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
import com.yusufteker.worthy.core.presentation.components.AppTopBar
import org.koin.compose.viewmodel.koinViewModel
import com.yusufteker.worthy.app.navigation.NavigationHandler
import com.yusufteker.worthy.app.navigation.NavigationModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.yusufteker.worthy.core.domain.getCurrentAppDate
import com.yusufteker.worthy.core.domain.model.format
import com.yusufteker.worthy.core.domain.model.isActive
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.components.WheelDatePickerV2
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.util.formatted
import com.yusufteker.worthy.screen.subscription.add.presentation.components.toComposeColor
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.activate
import worthy.composeapp.generated.resources.cancel
import worthy.composeapp.generated.resources.card_id
import worthy.composeapp.generated.resources.confirm
import worthy.composeapp.generated.resources.end_date
import worthy.composeapp.generated.resources.monthly_price
import worthy.composeapp.generated.resources.payment_day
import worthy.composeapp.generated.resources.pick_end_date
import worthy.composeapp.generated.resources.pick_start_date
import worthy.composeapp.generated.resources.start_date
import worthy.composeapp.generated.resources.terminate


//TODO GEÇMİŞ ÖDEMELERİNİ GÖRECEK GEÇMİŞ FİYATY ilet
// fiyat değişikliği yaptığında eski fiyatı aynı group id ile eklenecek.
// buraya gelirken aynı group id içinde olan güncel fiyatlı olan gösterilecek başta
@Composable
fun SubscriptionDetailScreenRoot(
    subscriptionId: Int,
    viewModel: SubscriptionDetailViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    onNavigateTo: (NavigationModel) -> Unit,


) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    NavigationHandler(viewModel){ model ->
        onNavigateTo(model)
    }
    viewModel.onAction(SubscriptionDetailAction.Init(subscriptionId))
    BaseContentWrapper(state = state) {
        SubscriptionDetailScreen(
            state = state,
            onAction = viewModel::onAction,
            contentPadding = contentPadding
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionDetailScreen(
    state: SubscriptionDetailState,
    onAction: (action: SubscriptionDetailAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize().padding(contentPadding).clickable{showBottomSheet = false},
        topBar = {
            AppTopBar(
                title = state.subscription?.name ?: "",
                onNavIconClick = {
                    onAction(SubscriptionDetailAction.NavigateBack)
                }
            )
        }
    ) { innerPadding ->


        state.subscription?.let { subscription ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(AppColors.background)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
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
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    subscription.colorHex?.toComposeColor() ?: AppColors.primary,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
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
                        Text(UiText.StringResourceId(Res.string.monthly_price).asString(), style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
                        Text(
                            text = subscription.amount.formatted(),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.primary
                        )
                    }
                }


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
                        DetailRow(UiText.StringResourceId(Res.string.start_date).asString(), subscription.startDate.format())
                        subscription.endDate?.let {
                            DetailRow(UiText.StringResourceId(Res.string.end_date).asString(), it.format())
                        }
                        subscription.scheduledDay?.let {
                            DetailRow(UiText.StringResourceId(Res.string.payment_day).asString(), "$it")
                        }
                        subscription.cardId?.let {
                            DetailRow(UiText.StringResourceId(Res.string.card_id).asString(), "$it")
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                // Aktivasyon / Sonlandırma Butonu
                Button(
                    onClick = {  showBottomSheet = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (subscription.isActive()) Color.Red else AppColors.primary
                    )
                ) {
                    Text(if (subscription.isActive()) UiText.StringResourceId(Res.string.terminate).asString() else UiText.StringResourceId(Res.string.activate).asString())
                }
            }
        }

        // Bottom Sheet
        if (showBottomSheet && state.subscription != null) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = rememberModalBottomSheetState()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        if (state.subscription.isActive()) UiText.StringResourceId(Res.string.pick_end_date).asString()
                        else UiText.StringResourceId(Res.string.pick_start_date).asString(),
                        style = MaterialTheme.typography.titleMedium
                    )


                    WheelDatePickerV2(
                        initialDate = getCurrentAppDate(),
                        onDateSelected = { pickedDate ->
                            onAction(SubscriptionDetailAction.OnDateSelected(pickedDate))
                        },
                        showDay = false
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showBottomSheet = false }) {
                            Text(UiText.StringResourceId(Res.string.cancel).asString())
                        }
                        Button(
                            onClick = {

                                if (state.subscription.isActive()) {
                                    onAction(SubscriptionDetailAction.EndSubscription(state.pickedDate))
                                } else {
                                    onAction(SubscriptionDetailAction.ActivateSubscription(state.pickedDate))
                                }
                                showBottomSheet = false
                            }
                        ) {
                            Text(UiText.StringResourceId(Res.string.confirm).asString())
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
    }
}
