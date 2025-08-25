package com.yusufteker.worthy.screen.wishlist.list.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.app.navigation.NavigationHandler
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.asStringList
import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
import com.yusufteker.worthy.core.presentation.components.EmptyScreen
import com.yusufteker.worthy.core.presentation.components.InteractiveSearchBar
import com.yusufteker.worthy.core.presentation.components.SwipeToDeleteWrapper
import com.yusufteker.worthy.core.presentation.theme.Constants.EMPTY_SCREEN_SIZE
import com.yusufteker.worthy.screen.wishlist.list.domain.generalSuggestions
import com.yusufteker.worthy.screen.wishlist.list.presentation.components.WishlistItemCard
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.no_wish
import worthy.composeapp.generated.resources.search_placeholder
import worthy.composeapp.generated.resources.wishlist_empty_screen_button

@Composable
fun WishlistScreenRoot(

    viewModel: WishlistViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    onNavigateTo: (route: Routes) -> Unit = {},
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    NavigationHandler(viewModel) { route, data ->
        onNavigateTo(route)
    }

    BaseContentWrapper(
        state = state
    ) {
        WishlistScreen(
            state = state, onAction = viewModel::onAction, contentPadding = contentPadding
        )
    }

}

@Composable
fun WishlistScreen(
    state: WishlistState,
    onAction: (WishlistAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
) {

    Scaffold(modifier = Modifier.padding(contentPadding), floatingActionButton = {
        WishlistFab(
            onClick = { onAction(WishlistAction.OnFabClick) })

    }, topBar = {
        InteractiveSearchBar(
            query = state.searchText,
            onSearchQueryChange = {
                onAction(WishlistAction.OnSearchTextChange(it))
            },
            searchResult = state.searchResults,
            searchHistory = state.searchHistory,
            searchSuggestions = generalSuggestions.asStringList(),
            onHistoryItemClick = { historyItem ->
                onAction(WishlistAction.OnSearchTextChange(historyItem))
            },
            placeholder = UiText.StringResourceId(Res.string.search_placeholder).asString(),
        )
    }) { innerPadding ->

        Column(Modifier.fillMaxSize()) {
            if (state.filteredItems.isEmpty()) {

                EmptyScreen(
                    icon = {
                        Icon(
                            painter = painterResource(Res.drawable.no_wish),
                            contentDescription = "Add Wishlist Item",
                            modifier = Modifier.size(EMPTY_SCREEN_SIZE)
                        )
                    },
                    buttonText = UiText.StringResourceId(Res.string.wishlist_empty_screen_button)
                        .asString(),
                    onButtonClick = {
                        onAction(WishlistAction.OnFabClick)
                    })
            } else if (state.filteredItems.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.padding(
                        top = innerPadding.calculateTopPadding(),
                        bottom = innerPadding.calculateBottomPadding()
                    )
                ) {

                    items(
                        items = state.filteredItems, key = { it.id }) { item ->

                        SwipeToDeleteWrapper(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            shape = CardDefaults.shape,
                            onDelete = {
                                onAction(WishlistAction.OnItemDelete(item.id))
                            },
                        ) {
                            WishlistItemCard(
                                item = item,
                                onCheckedChange = {
                                    onAction(WishlistAction.OnIsItemPurchasedChange(item, it))
                                },
                                onClick = {
                                    onAction(WishlistAction.OnItemClick(item.id))
                                },
                            )
                        }

                    }
                }

            }
        }

    }

}

@Composable
fun WishlistFab(
    onClick: () -> Unit
) {
    // Floating action button implementation
    // This could be a simple button or a custom FAB component
    FloatingActionButton(
        onClick = onClick, content = {
            // Icon or text for the FAB
            Icon(
                imageVector = Icons.Default.Add, contentDescription = "Add Wishlist Item"
            )
        })
}
