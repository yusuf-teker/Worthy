package com.yusufteker.worthy.screen.wishlist.add.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.core.presentation.components.ImagePickerComponent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WishlistAddScreenRoot(
    viewModel: WishlistAddViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    WishlistAddScreen(state = state, onAction = viewModel::onAction, contentPadding = contentPadding)
}

@Composable
fun WishlistAddScreen(
    state: WishlistAddState,
    onAction: (action: WishlistAddAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val size = minOf(maxWidth, maxHeight)

            ImagePickerComponent(
                selectedImage = state.imageBitmap,
                onImageSelected = { onAction(WishlistAddAction.OnImageSelected(it)) },
                modifier = Modifier.size(size)
            )
        }


    }
}