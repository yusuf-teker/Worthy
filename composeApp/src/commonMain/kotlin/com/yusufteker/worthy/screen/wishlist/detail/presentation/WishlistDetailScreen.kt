package com.yusufteker.worthy.screen.wishlist.detail.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.app.navigation.NavigationHandler
import com.yusufteker.worthy.app.navigation.NavigationModel
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WishlistDetailScreenRoot(
    viewModel: WishlistDetailViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    wishlistId: Int,
    onNavigateTo: (NavigationModel) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    viewModel.onAction(WishlistDetailAction.Init(wishlistId))
    NavigationHandler(viewModel){ model ->
        onNavigateTo(model)
    }

    BaseContentWrapper(
        state = state
    ) {
        WishlistDetailScreen(
            state = state,
            onAction = viewModel::onAction,
            contentPadding = contentPadding
        )
    }
}

@Composable
fun WishlistDetailScreen(
    state: WishlistDetailState,
    onAction: (action: WishlistDetailAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(state.wishlistItemId.toString())
    }
}