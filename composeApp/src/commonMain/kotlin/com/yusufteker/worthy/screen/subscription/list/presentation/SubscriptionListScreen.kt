package com.yusufteker.worthy.screen.subscription.list.presentation


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.yusufteker.worthy.core.presentation.components.EmptyScreen
import com.yusufteker.worthy.core.presentation.components.SwipeToDeleteWrapper
import com.yusufteker.worthy.core.presentation.theme.Constants.EMPTY_SCREEN_SIZE
import com.yusufteker.worthy.screen.subscription.add.presentation.AddSubscriptionAction
import com.yusufteker.worthy.screen.subscription.list.presentation.components.SubscriptionItem
import com.yusufteker.worthy.screen.wishlist.list.presentation.WishlistAction
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.subscription_empty_screen_button
import worthy.composeapp.generated.resources.screen_title_subscription_list
import worthy.composeapp.generated.resources.subscription

@Composable
fun SubscriptionListScreenRoot(
    viewModel: SubscriptionListViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    onNavigateTo: (route: Routes) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NavigationHandler(viewModel){ route, data ->
        onNavigateTo(route)
    }
    BaseContentWrapper(state = state) {
        SubscriptionListScreen(
            state = state,
            onAction = viewModel::onAction,
            contentPadding = contentPadding
        )
    }
}

@Composable
fun SubscriptionListScreen(
    state: SubscriptionListState,
    onAction: (action: SubscriptionListAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {

Scaffold(
    modifier = Modifier.fillMaxSize().padding(contentPadding),
    topBar = {
        AppTopBar(
            title = UiText.StringResourceId(Res.string.screen_title_subscription_list).asString(),
            onNavIconClick = {
                onAction(SubscriptionListAction.OnNavigateBack)
            }
        )
    }  ,  floatingActionButton = {
        FloatingActionButton(
            onClick = {
                onAction(SubscriptionListAction.NavigateToAddSubscription)
            }, content = {
                // Icon or text for the FAB
                Icon(
                    imageVector = Icons.Default.Add, contentDescription = "Add Subscription"
                )
            })
    }
){
  Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

      if (state.subscriptions.isEmpty()){
          EmptyScreen(
              icon = {
                  Icon(
                      painter = painterResource(Res.drawable.subscription),
                      contentDescription = null,
                      modifier = Modifier.size(EMPTY_SCREEN_SIZE)
                  )
              },
              buttonText = UiText.StringResourceId(Res.string.subscription_empty_screen_button).asString(),

          ) {
              onAction(SubscriptionListAction.OnAddNewSubscription)
          }
      }else{
          LazyColumn {
              items(
                  items = state.subscriptions, key = { it.id }) { subscription ->

                  SwipeToDeleteWrapper(
                      modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                      shape = CardDefaults.shape,
                      onDelete = {
                          onAction(SubscriptionListAction.OnItemDelete(subscription.id))
                      },
                  ) {
                      SubscriptionItem(subscription = subscription)

                  }
              }
          }
      }

    }
}
  
}