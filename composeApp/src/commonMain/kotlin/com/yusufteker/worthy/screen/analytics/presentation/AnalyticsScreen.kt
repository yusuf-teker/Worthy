package com.yusufteker.worthy.screen.analytics.presentation


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
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
import com.yusufteker.worthy.core.presentation.components.SwipeToDeleteWrapper
import com.yusufteker.worthy.screen.analytics.presentation.components.TransactionFilter
import com.yusufteker.worthy.screen.analytics.presentation.components.TransactionListItem
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.add_new_card
import worthy.composeapp.generated.resources.filter
import worthy.composeapp.generated.resources.screen_name_analytics

@Composable
fun AnalyticsScreenRoot(
    viewModel: AnalyticsViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    onNavigateTo: (route: Routes) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NavigationHandler(viewModel){ route, data->
        onNavigateTo(route)
    }

    BaseContentWrapper(state = state) {
        AnalyticsScreen(
            state = state,
            onAction = viewModel::onAction,
            contentPadding = contentPadding
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    state: AnalyticsState,
    onAction: (action: AnalyticsAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {

    var showFilter by remember { mutableStateOf(false) }


    Scaffold(
    modifier = Modifier.fillMaxSize().padding(contentPadding),
    topBar = {
        AppTopBar(
            title = UiText.StringResourceId(Res.string.screen_name_analytics).asString(),
            onNavIconClick = {
                onAction(AnalyticsAction.NavigateBack)
            },
            actions = {
                IconButton(
                    onClick = {
                        showFilter = !showFilter
                    }
                ){
                    Icon(
                        painter = painterResource(resource = Res.drawable.filter),
                        contentDescription = "filter",
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

      LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
          items(state.transactions) { transaction ->
              SwipeToDeleteWrapper(
                  modifier = Modifier.fillMaxWidth(),
                  shape = CardDefaults.shape,
                  onDelete = {
                      onAction(AnalyticsAction.OnItemDelete(transaction.id))
                  },
              ) {
                  TransactionListItem(transaction = transaction)

              }
          }
      }

    }

        if (showFilter) {
            ModalBottomSheet(
                onDismissRequest = { showFilter = false },
            ) {
                TransactionFilter(
                    categories = state.categories,
                    cards = state.cards,
                    selectedCategories = state.selectedCategories,
                    selectedCards = state.selectedCards,
                    selectedDateRange = state.selectedDateRange,
                    onDateRangeSelected = { onAction(AnalyticsAction.OnDateRangeSelected(it)) },
                    onCategorySelected = { category, isSelected ->
                        onAction(AnalyticsAction.OnCategorySelected(category, isSelected))

                    },
                    onCardSelected = { card, isSelected ->
                        onAction(AnalyticsAction.OnCardSelected(card, isSelected))
                    },
                    clearFilters = {
                        onAction(AnalyticsAction.ClearFilters)
                    }
                )
            }
        }
}
  
}