package com.yusufteker.worthy.screen.card.list.presentation


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.app.navigation.NavigationHandler
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
import com.yusufteker.worthy.core.presentation.components.AppTopBar
import com.yusufteker.worthy.screen.card.list.presentation.components.CreditCardCarousel
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.add_new_card
import worthy.composeapp.generated.resources.my_cards
import worthy.composeapp.generated.resources.screen_title_my_cards

@Composable
fun CardListScreenRoot(
    viewModel: CardListViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    onNavigateTo: (Routes) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NavigationHandler(viewModel){route, data ->
        onNavigateTo(route)
    }

    BaseContentWrapper(state = state) {
        CardListScreen(
            state = state,
            onAction = viewModel::onAction,
            contentPadding = contentPadding
        )
    }
}

@Composable
fun CardListScreen(
    state: CardListState,
    onAction: (action: CardListAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {

Scaffold(
    modifier = Modifier.fillMaxSize().padding(contentPadding),
    topBar = {
        AppTopBar(
            title = UiText.StringResourceId(Res.string.screen_title_my_cards).asString(),
            onNavIconClick = {
                onAction(CardListAction.NavigateBack)
            },
            actions = {
                IconButton(
                    onClick = {
                        onAction(CardListAction.AddNewCard)
                    }
                ){
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = UiText.StringResourceId(Res.string.add_new_card).asString(),
                    )
                }

            }
        )
    }
){
  Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

      if (state.cards.isNotEmpty()){
          CreditCardCarousel(
              modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
              cards = state.cards
          )
      }


    }
}
  
}