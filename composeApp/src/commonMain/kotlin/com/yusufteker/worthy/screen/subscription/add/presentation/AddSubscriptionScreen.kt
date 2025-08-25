package com.yusufteker.worthy.screen.subscription.add.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.app.navigation.NavigationHandler
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.domain.getCurrentAppDate
import com.yusufteker.worthy.core.domain.getCurrentYear
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.emptyMoney
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
import com.yusufteker.worthy.core.presentation.capitalizeWords
import com.yusufteker.worthy.core.presentation.components.AppButton
import com.yusufteker.worthy.core.presentation.components.AppTopBar
import com.yusufteker.worthy.core.presentation.components.CardSelector
import com.yusufteker.worthy.core.presentation.components.DateSelector
import com.yusufteker.worthy.core.presentation.components.DayOfMonthSelector
import com.yusufteker.worthy.core.presentation.components.MoneyInput
import com.yusufteker.worthy.core.presentation.components.WheelDatePicker
import com.yusufteker.worthy.core.presentation.components.WheelDatePickerV2
import com.yusufteker.worthy.screen.subscription.add.presentation.components.ColorPicker
import com.yusufteker.worthy.screen.subscription.add.presentation.components.SubscriptionCategorySelector
import com.yusufteker.worthy.screen.subscription.add.presentation.components.toComposeColor
import com.yusufteker.worthy.screen.subscription.add.presentation.components.toHexString
import com.yusufteker.worthy.screen.subscription.list.presentation.components.SubscriptionItem
import com.yusufteker.worthy.screen.wishlist.list.presentation.WishlistAction
import com.yusufteker.worthy.screen.wishlist.list.presentation.WishlistFab
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.add
import worthy.composeapp.generated.resources.monthly_price
import worthy.composeapp.generated.resources.repeat_day
import worthy.composeapp.generated.resources.screen_title_add_new_subscription
import worthy.composeapp.generated.resources.service_name
import worthy.composeapp.generated.resources.subscription_end_date
import worthy.composeapp.generated.resources.subscription_start_date
import worthy.composeapp.generated.resources.toggle_card_details_show
import worthy.composeapp.generated.resources.toggle_card_details_skip
import worthy.composeapp.generated.resources.toggle_optional_info_hide
import worthy.composeapp.generated.resources.toggle_optional_info_show

@Composable
fun AddSubscriptionScreenRoot( // TODO BASTAN SONRA TEKRAR BAKILACAK DUMMY YAZILDI
    viewModel: AddSubscriptionViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    onNavigateTo: (route: Routes) -> Unit

) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NavigationHandler(viewModel) { route, data ->
        onNavigateTo(route)
    }

    BaseContentWrapper(state = state) {
        AddSubscriptionScreen(
            state = state,
            onAction = viewModel::onAction,
            contentPadding = contentPadding
        )
    }
}

@Composable
fun AddSubscriptionScreen(
    state: AddSubscriptionState,
    onAction: (action: AddSubscriptionAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {

    Scaffold(
        modifier = Modifier.fillMaxSize().padding(contentPadding),
        topBar = {
            AppTopBar(
                title = UiText.StringResourceId(Res.string.screen_title_add_new_subscription)
                    .asString(),
                onNavIconClick = {
                    onAction(AddSubscriptionAction.NavigateBack)
                }
            )
        },
        bottomBar = {
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = UiText.StringResourceId(Res.string.add).asString(),
                onClick = { onAction(AddSubscriptionAction.SubmitSubscription) }
            )
        },

    ) {

        var expanded by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {


            SubscriptionItem(
                subscription = state.subscriptionPrev,
                isPreview = true
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = state.subscriptionName,
                    onValueChange = { name ->
                        onAction(
                            AddSubscriptionAction.OnNameChanged(
                                name.capitalizeWords()
                            )
                        )
                    },
                    label = { Text(UiText.StringResourceId(Res.string.service_name).asString()) },
                    singleLine = true
                )
                ColorPicker(
                    modifier = Modifier.padding(8.dp),
                    selectedColor = state.subscriptionPrev.color?.toComposeColor(),
                    onColorSelected = {
                        onAction(AddSubscriptionAction.OnColorChanged(it.toHexString()))
                    }
                )
            }



            SubscriptionCategorySelector(
                categories = state.categories,
                selectedCategory = state.selectedCategory,
                onCategorySelected = {
                    onAction(AddSubscriptionAction.OnCategorySelected(it))
                },
                onNewCategoryCreated = {
                    onAction(AddSubscriptionAction.OnNewCategoryCreated(it))
                }
            )

            MoneyInput(
                modifier = Modifier.fillMaxWidth(),
                money = state.price,
                onValueChange = {
                    onAction(
                        AddSubscriptionAction.OnPriceChanged(
                            it ?: emptyMoney()
                        )
                    )
                },
                label = UiText.StringResourceId(Res.string.monthly_price)
            )

            WheelDatePickerV2(
                title =   UiText.StringResourceId(Res.string.subscription_start_date).asString(),
                initialDate = state.startDate,
                onDateSelected = {
                    onAction(AddSubscriptionAction.OnStartDateChanged(it))
                },
                maxDate = state.endDate?: AppDate(2100, 12, 31),
                modifier = Modifier.fillMaxWidth(),
            )
            // todo odeme gunu lableı olacak
            DayOfMonthSelector(
                selectedDay = state.scheduledDay,
                onDayChange = { onAction(AddSubscriptionAction.OnScheduledDayChanged(it?:1)) },
                label = UiText.StringResourceId(Res.string.repeat_day)
            )

            Row(
                modifier = Modifier.fillMaxWidth().clickable{  expanded = !expanded }.padding(start = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text =  if (expanded) {
                        UiText.StringResourceId(Res.string.toggle_optional_info_hide).asString()
                    } else {
                        UiText.StringResourceId(Res.string.toggle_optional_info_show).asString()
                    }, modifier = Modifier.weight(1f)
                )

                val rotation by animateFloatAsState(
                    if (expanded) 180f else 0f, label = "arrowAnim"
                )

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.rotate(rotation)
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    CardSelector(
                        cards = state.cards,
                        selectedCard = state.selectedCard,
                        onCardSelected = {
                            onAction(AddSubscriptionAction.OnCardSelected(it))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        onAddNewCard = {
                            //onAddNewCardClicked.invoke()
                        }
                    )
                    Spacer(Modifier.height(16.dp))
                    WheelDatePickerV2(
                        title =   UiText.StringResourceId(Res.string.subscription_end_date).asString(),
                        initialDate = state.endDate ?: getCurrentAppDate(),
                        onDateSelected = {
                            onAction(AddSubscriptionAction.OnEndDateChanged(it))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minDate = state.startDate

                    )
                }

            }



        }

    }

}