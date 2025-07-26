package com.yusufteker.worthy.screen.wishlist.detail.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WishlistDetailScreenRoot(
    viewModel: WishlistDetailViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    WishlistDetailScreen(state = state, onAction = viewModel::onAction, contentPadding = contentPadding)
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
        // TODO
    }
}