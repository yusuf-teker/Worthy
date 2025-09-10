package com.yusufteker.worthy.screen.subscription.detail.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.yusufteker.worthy.core.presentation.components.AppSurface
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
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.activate
import worthy.composeapp.generated.resources.amount_must_be_greater_than_zero
import worthy.composeapp.generated.resources.cancel
import worthy.composeapp.generated.resources.card_name
import worthy.composeapp.generated.resources.confirm
import worthy.composeapp.generated.resources.date_ranges_conflict
import worthy.composeapp.generated.resources.edit
import worthy.composeapp.generated.resources.end_date
import worthy.composeapp.generated.resources.end_date_required_for_past_start
import worthy.composeapp.generated.resources.ic_calendar
import worthy.composeapp.generated.resources.ic_credit_card
import worthy.composeapp.generated.resources.ic_schedule
import worthy.composeapp.generated.resources.ic_trending_up
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
import worthy.composeapp.generated.resources.subscription_history
import worthy.composeapp.generated.resources.terminate
import worthy.composeapp.generated.resources.update
import worthy.composeapp.generated.resources.update_current_subscription_title
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

    //  gradient background
    Box(
        modifier = Modifier.fillMaxSize().background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    AppColors.surface,
                    AppColors.surfaceVariant.copy(alpha = 0.3f),
                    AppColors.surface
                )
            )
        )
    ) {
        AppScaffold(modifier = modifier.padding(contentPadding), topBar = {
            AppTopBar(
                title = state.subscription?.name ?: "",
                onNavIconClick = { onAction(SubscriptionDetailAction.NavigateBack) },
                actions = {
                    if (state.subscription != null) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Transaction",
                            modifier = Modifier.size(28.dp).clickable {
                                onAction(
                                    SubscriptionDetailAction.OnDeleteGroupRecurringItem(
                                        state.subscription.groupId
                                    )
                                )
                            }.padding(2.dp),
                            tint = AppColors.icon_red
                        )
                    }
                })
        }, bottomBar = {
            //  Action Buttons
            state.subscription?.let {
                Column {
                    // Edit Button with  styling
                    AppButton( // EDIT
                        text = UiText.StringResourceId(Res.string.edit).asString(), onClick = {
                            showHistoryEditor = true
                        }, trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit history"
                            )

                        }, loading = state.isLoading, modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    AppButton( // ACTIVATION
                        text = if (state.subscription.isActive()) UiText.StringResourceId(Res.string.terminate)
                            .asString() else UiText.StringResourceId(Res.string.activate)
                            .asString(),
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

        }) { innerPadding ->
            state.subscription?.let { subscription ->
                LazyColumn(
                    modifier = Modifier.padding(innerPadding).fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item { Spacer(Modifier.height(8.dp)) }

                    //  Header Card with gradient and shadow
                    item {
                        AnimatedVisibility(
                            visible = true,
                            enter = slideInVertically() + fadeIn(),
                            exit = slideOutVertically() + fadeOut()
                        ) {
                            HeaderCard(subscription = subscription)
                        }
                    }

                    //  Price Card with animated accent
                    item {
                        AnimatedVisibility(
                            visible = true,
                            enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
                            exit = slideOutVertically() + fadeOut()
                        ) {
                            PriceCard(subscription = subscription)
                        }
                    }

                    //  Details Card with icons
                    item {
                        AnimatedVisibility(
                            visible = true,
                            enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
                            exit = slideOutVertically() + fadeOut()
                        ) {
                            DetailsCard(
                                subscription = subscription,
                                activeStreak = state.activeStreak,
                                cardName = state.card?.nickname
                            )
                        }
                    }

                    //  Chart Card
                    item {
                        AnimatedVisibility(
                            visible = true,
                            enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
                            exit = slideOutVertically() + fadeOut()
                        ) {
                            ChartCard(data = state.subscriptions.toMonthlyData())
                        }
                    }



                    item { Spacer(Modifier.height(32.dp)) }
                }
            }
        }

        // Bottom Sheets remain the same...
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
                onDismiss = { showTerminateActivateBottomSheet = false })
        }

        if (showHistoryEditor && state.subscription != null) {
            SubscriptionHistoryEditor(
                history = state.subscriptions,
                onDismiss = { showHistoryEditor = false },
                onSave = { items ->
                    onAction(SubscriptionDetailAction.OnUpdateRecurringItems(items))
                })
        }
    }
}

@Composable
private fun HeaderCard(subscription: RecurringItem.Subscription) {
    val dominantColor = subscription.colorHex?.toComposeColor() ?: AppColors.primary

    Card(
        modifier = Modifier.fillMaxWidth().shadow(
            elevation = 16.dp,
            shape = RoundedCornerShape(24.dp),
            ambientColor = dominantColor.copy(alpha = 0.1f),
            spotColor = dominantColor.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface
        ),
    ) {
        Box {
            // Gradient overlay
            Box(
                modifier = Modifier.fillMaxWidth().height(140.dp).background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            dominantColor.copy(alpha = 0.05f), dominantColor.copy(alpha = 0.15f)
                        )
                    ), shape = RoundedCornerShape(24.dp)
                )
            )

            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                //  icon with glow effect
                Box(
                    modifier = Modifier.size(80.dp).shadow(
                        elevation = 12.dp,
                        shape = CircleShape,
                        ambientColor = dominantColor.copy(alpha = 0.3f),
                        spotColor = dominantColor.copy(alpha = 0.3f)
                    ).background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                dominantColor, dominantColor.copy(alpha = 0.8f)
                            )
                        ), shape = CircleShape
                    ), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = subscription.icon,
                        style = AppTypography.headlineLarge,
                        color = Color.White
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        subscription.name,
                        style = AppTypography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.onSurface
                    )
                    subscription.category?.let {
                        Surface(
                            color = dominantColor.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                it.name,
                                style = AppTypography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = dominantColor,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PriceCard(subscription: RecurringItem.Subscription) {
    val scale by animateFloatAsState(
        targetValue = 1f, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
        )
    )

    Card(
        modifier = Modifier.fillMaxWidth().scale(scale).shadow(
            elevation = 12.dp,
            shape = RoundedCornerShape(24.dp),
            ambientColor = AppColors.primary.copy(alpha = 0.1f)
        ), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(
            containerColor = AppColors.primary.copy(alpha = 0.05f)
        )
    ) {
        Box {
            // Animated background pattern
            val color = AppColors.primary
            Canvas(
                modifier = Modifier.fillMaxWidth().height(140.dp).alpha(0.1f)
            ) {
                val path = Path()
                path.moveTo(0f, size.height * 0.7f)
                path.quadraticBezierTo(
                    size.width * 0.25f, size.height * 0.3f, size.width * 0.5f, size.height * 0.6f
                )
                path.quadraticBezierTo(
                    size.width * 0.75f, size.height * 0.9f, size.width, size.height * 0.4f
                )
                path.lineTo(size.width, size.height)
                path.lineTo(0f, size.height)
                path.close()

                drawPath(path, color = color)
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painterResource(Res.drawable.ic_trending_up),
                        contentDescription = null,
                        tint = AppColors.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        UiText.StringResourceId(Res.string.monthly_price).asString(),
                        style = AppTypography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = AppColors.onSurface.copy(alpha = 0.7f)
                    )
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = subscription.amount.formatted(),
                    style = AppTypography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.primary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun DetailsCard(
    subscription: RecurringItem.Subscription, activeStreak: Int?, cardName: String? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.surface)
    ) {
        Column(
            modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            activeStreak?.let {
                StreakBadge(it, Modifier.align(Alignment.End))
            }

            DetailRow(
                iconPainter = painterResource(Res.drawable.ic_calendar),
                label = UiText.StringResourceId(Res.string.start_date).asString(),
                value = subscription.startDate.format(showDay = false),
                iconColor = AppColors.primary
            )

            subscription.endDate?.let {
                DetailRow(
                    iconPainter = painterResource(Res.drawable.ic_calendar),
                    label = UiText.StringResourceId(Res.string.end_date).asString(),
                    value = it.format(showDay = false),
                    iconColor = AppColors.error
                )
            }

            subscription.scheduledDay?.let {
                DetailRow(
                    iconPainter = painterResource(Res.drawable.ic_schedule),
                    label = UiText.StringResourceId(Res.string.payment_day).asString(),
                    value = "$it",
                )
            }

            cardName?.let {
                DetailRow(
                    iconPainter = painterResource(Res.drawable.ic_credit_card),
                    label = UiText.StringResourceId(Res.string.card_name).asString(),
                    value = it,
                    iconColor = AppColors.primary
                )
            }
        }
    }
}

@Composable
private fun DetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    iconPainter: androidx.compose.ui.graphics.painter.Painter? = null,
    label: String,
    value: String,
    iconColor: Color = AppColors.primary
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = iconColor.copy(alpha = 0.1f)
            ) {
                when {
                    icon != null -> {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = iconColor,
                            modifier = Modifier.size(24.dp).padding(4.dp)
                        )
                    }

                    iconPainter != null -> {
                        Icon(
                            painter = iconPainter,
                            contentDescription = label,
                            tint = iconColor,
                            modifier = Modifier.size(24.dp).padding(4.dp)
                        )
                    }
                }
            }
            Text(
                label,
                style = AppTypography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = AppColors.onSurface.copy(alpha = 0.7f)
            )
        }

        Surface(
            shape = RoundedCornerShape(12.dp), color = AppColors.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Text(
                value,
                style = AppTypography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = AppColors.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun StreakBadge(activeMonths: Int, modifier: Modifier = Modifier) {
    if (activeMonths > 0) {
        AppSurface(
            color = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFF4CAF50), Color(0xFF8BC34A)
                )
            ), shape = RoundedCornerShape(50), modifier = modifier.shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(50),
                ambientColor = Color(0xFF4CAF50).copy(alpha = 0.3f)
            )
        ) {
            Text(
                text = UiText.StringResourceId(Res.string.streak_text, arrayOf(activeMonths))
                    .asString(),
                style = AppTypography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun ChartCard(data: List<Pair<AppDate, Money>>) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painterResource(Res.drawable.ic_trending_up),
                    contentDescription = null,
                    tint = AppColors.primary
                )
                Text(
                    UiText.StringResourceId(Res.string.subscription_history).asString(),
                    style = AppTypography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.onSurface
                )
            }

            Spacer(Modifier.height(16.dp))

            MiniSubscriptionChart(data = data, height = 140.dp)
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

    Column(modifier = modifier) {
        Box(modifier = Modifier.height(height).fillMaxWidth()) {
            //  gradient background
            Box(
                modifier = Modifier.fillMaxSize().background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            lineColor.copy(alpha = 0.1f), Color.Transparent
                        )
                    ), shape = RoundedCornerShape(12.dp)
                ).clip(RoundedCornerShape(12.dp))
            )

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
                    return if (maxY == minY) {
                        heightPx / 2f
                    } else {
                        heightPx - paddingY - ((amount - minY) / (maxY - minY) * chartHeight).toFloat()
                    }
                }

                // Draw gradient fill under the line
                val fillPath = Path()
                var fillPathStarted = false

                sortedData.forEachIndexed { index, (date, money) ->
                    val x = mapX(index)
                    val y = mapY(money.amount)

                    val shouldBreakLine = if (index > 0) {
                        val previousDate = sortedData[index - 1].first
                        !isConsecutiveMonth(previousDate, date)
                    } else false

                    if (shouldBreakLine && fillPathStarted) {
                        fillPath.lineTo(mapX(index - 1), heightPx - paddingY)
                        fillPath.lineTo(paddingX, heightPx - paddingY)
                        fillPath.close()

                        drawPath(
                            fillPath, brush = Brush.verticalGradient(
                                colors = listOf(
                                    lineColor.copy(alpha = 0.3f), Color.Transparent
                                )
                            )
                        )
                        fillPath.reset()
                        fillPathStarted = false
                    }

                    if (!fillPathStarted) {
                        fillPath.moveTo(x, heightPx - paddingY)
                        fillPath.lineTo(x, y)
                        fillPathStarted = true
                    } else {
                        fillPath.lineTo(x, y)
                    }
                }

                if (fillPathStarted) {
                    fillPath.lineTo(mapX(sortedData.lastIndex), heightPx - paddingY)
                    fillPath.close()
                    drawPath(
                        fillPath, brush = Brush.verticalGradient(
                            colors = listOf(
                                lineColor.copy(alpha = 0.3f), Color.Transparent
                            )
                        )
                    )
                }

                // Draw the main line with  styling
                var currentPath = Path()
                var pathStarted = false

                sortedData.forEachIndexed { index, (date, money) ->
                    val x = mapX(index)
                    val y = mapY(money.amount)

                    val shouldBreakLine = if (index > 0) {
                        val previousDate = sortedData[index - 1].first
                        !isConsecutiveMonth(previousDate, date)
                    } else false

                    if (shouldBreakLine && pathStarted) {
                        drawPath(
                            currentPath, color = lineColor, style = Stroke(
                                width = 6f, cap = StrokeCap.Round
                            )
                        )
                        currentPath = Path()
                        pathStarted = false
                    }

                    if (!pathStarted) {
                        currentPath.moveTo(x, y)
                        pathStarted = true
                    } else {
                        currentPath.lineTo(x, y)
                    }
                }

                if (pathStarted) {
                    drawPath(
                        currentPath, color = lineColor, style = Stroke(
                            width = 6f, cap = StrokeCap.Round
                        )
                    )
                }

                //  data points
                sortedData.forEachIndexed { index, (_, money) ->
                    val x = mapX(index)
                    val y = mapY(money.amount)

                    // Outer ring
                    drawCircle(
                        color = lineColor.copy(alpha = 0.3f),
                        radius = 8f,
                        center = androidx.compose.ui.geometry.Offset(x, y)
                    )
                    // Inner circle
                    drawCircle(
                        color = lineColor,
                        radius = 5f,
                        center = androidx.compose.ui.geometry.Offset(x, y)
                    )
                    // Center highlight
                    drawCircle(
                        color = Color.White,
                        radius = 2f,
                        center = androidx.compose.ui.geometry.Offset(x, y)
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        //  X-axis labels
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            sortedData.forEach { (date, _) ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AppColors.surfaceVariant.copy(alpha = 0.6f)
                ) {
                    Text(
                        getMonthShortName(date.month),
                        style = AppTypography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = AppColors.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

private fun isConsecutiveMonth(date1: AppDate, date2: AppDate): Boolean {
    val nextMonth = date1.nextMonth()
    return nextMonth.year == date2.year && nextMonth.month == date2.month
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
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                if (isActive) UiText.StringResourceId(Res.string.pick_end_date).asString()
                else UiText.StringResourceId(Res.string.pick_start_date).asString(),
                style = AppTypography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            WheelDatePickerV3(
                initialDate = pickedDate,
                onDateSelected = { date -> onDateSelected(date) },
                showDay = false
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextButton(
                    onClick = onDismiss, modifier = Modifier.weight(1f)
                ) {
                    Text(UiText.StringResourceId(Res.string.cancel).asString())
                }
                Button(
                    onClick = onConfirm, modifier = Modifier.weight(1f)
                ) {
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
                                    UiText.StringResourceId(Res.string.update_current_subscription_title)
                                        .asString(),
                                    style = AppTypography.titleMedium,
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

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        HorizontalDivider(
                            thickness = 2.dp, color = AppColors.outline.copy(alpha = 0.3f)
                        )
                        Text(
                            UiText.StringResourceId(Res.string.subscription_history).asString(),
                            style = AppTypography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AppColors.onSurface.copy(alpha = 0.7f),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }

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
                    style = AppTypography.titleSmall,
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
                            style = AppTypography.labelSmall,
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
                        style = AppTypography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Checkbox(
                        checked = hasEndDate, onCheckedChange = { isChecked ->
                            hasEndDate = isChecked
                            if (!isChecked) {
                                onUpdate(subscription.copy(endDate = null))
                            }
                        })
                    Text("Has end date", style = AppTypography.bodySmall)
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
                    text = it, color = AppColors.error, style = AppTypography.bodySmall
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

        if (currentStart == nextStart) {
            return Pair(
                current.id, UiText.StringResourceId(
                    id = Res.string.start_dates_cannot_be_same,
                    args = arrayOf(currentStart.month, currentStart.year)
                )
            )
        }

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