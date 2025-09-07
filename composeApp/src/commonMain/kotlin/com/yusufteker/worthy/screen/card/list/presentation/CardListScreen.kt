package com.yusufteker.worthy.screen.card.list.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.app.navigation.NavigationHandler
import com.yusufteker.worthy.app.navigation.NavigationModel
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.AppScaffold
import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
import com.yusufteker.worthy.core.presentation.components.AppTopBar
import com.yusufteker.worthy.core.presentation.components.EmptyScreen
import com.yusufteker.worthy.core.presentation.theme.Constants.EMPTY_SCREEN_SIZE
import com.yusufteker.worthy.screen.card.list.presentation.components.CreditCardCarousel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.add_new_card
import worthy.composeapp.generated.resources.add_your_first_card
import worthy.composeapp.generated.resources.empty_credit_card
import worthy.composeapp.generated.resources.screen_title_my_cards

@Composable
fun CardListScreenRoot(
    viewModel: CardListViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    onNavigateTo: (NavigationModel) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NavigationHandler(viewModel) { model ->
        onNavigateTo(model)
    }

    BaseContentWrapper(state = state) { modifier ->
        CardListScreen(
            modifier = modifier,
            state = state,
            onAction = viewModel::onAction,
            contentPadding = contentPadding
        )
    }
}

@Composable
fun CardListScreen(
    modifier: Modifier = Modifier,
    state: CardListState,
    onAction: (action: CardListAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {

    AppScaffold(
        modifier = modifier.padding(contentPadding), topBar = {
            AppTopBar(
                title = UiText.StringResourceId(Res.string.screen_title_my_cards).asString(),
                onNavIconClick = {
                    onAction(CardListAction.NavigateBack)
                },
                actions = {
                    IconButton(
                        onClick = {
                            onAction(CardListAction.AddNewCard)
                        }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = UiText.StringResourceId(Res.string.add_new_card)
                                .asString(),
                        )
                    }

                })
        }) {
        Column(
            modifier = modifier.padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (state.cards.isNotEmpty()) {
                CreditCardCarousel(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp), cards = state.cards
                )
            } else {
                EmptyScreen(
                    icon = {
                        Icon(
                            painter = painterResource(Res.drawable.empty_credit_card),
                            contentDescription = UiText.StringResourceId(Res.string.add_new_card)
                                .asString(),
                            modifier = Modifier.size(EMPTY_SCREEN_SIZE)
                        )
                    },
                    buttonText = UiText.StringResourceId(Res.string.add_your_first_card).asString()
                ) {
                    onAction(CardListAction.AddNewCard)
                }

            }

        }
    }

}