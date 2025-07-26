package com.yusufteker.worthy.screen.wishlist.list.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.presentation.UiEvent
import com.yusufteker.worthy.core.presentation.components.SearchBar
import com.yusufteker.worthy.core.presentation.theme.AppColors
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WishlistScreenRoot(

    viewModel: WishlistViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    navigateToWishlistDetail: (wishlistId: Int) -> Unit = {},
    navigateToWishlistAdd: () -> Unit = {}
){

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when(event){
                is UiEvent.NavigateTo -> {
                    if (event.route == Routes.WishlistAdd){
                        navigateToWishlistAdd()
                    }
                }
                else -> Unit
            }
        }
    }
    WishlistScreen(
        state = state,
        onAction = viewModel::onAction,
        contentPadding = contentPadding
    )
}

@Composable
fun WishlistScreen(

    state: WishlistState,
    onAction: (WishlistAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
){


    Scaffold(
        modifier = Modifier.padding(contentPadding),
        topBar = {
            SearchBar(
                text = state.searchText,
                onTextChange = { onAction(WishlistAction.OnSearchTextChange(it)) },
                onSearch = { onAction(WishlistAction.OnSearch) },
                onClear = { onAction(WishlistAction.OnClearSearch) },
                color = AppColors.background
            )
        },
        floatingActionButton = {
            WishlistFab(
                onClick = { onAction(WishlistAction.OnFabClick) }
            )

        }
    ) {

    }

}


@Composable
fun WishlistFab(
    onClick: () -> Unit
) {
    // Floating action button implementation
    // This could be a simple button or a custom FAB component
    FloatingActionButton(
        onClick = onClick,
        content = {
            // Icon or text for the FAB
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Wishlist Item"
            )
        }
    )
}
