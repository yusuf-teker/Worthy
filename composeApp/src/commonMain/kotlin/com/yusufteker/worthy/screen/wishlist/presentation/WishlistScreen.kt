package com.yusufteker.worthy.screen.wishlist.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WishlistScreenRoot(

    viewModel:WishlistViewModel = koinViewModel(),
    contentPadding : PaddingValues = PaddingValues(),
){

    val state by viewModel.state.collectAsStateWithLifecycle()


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

    Box{
        Text(text = "Wishlist Screen", modifier = Modifier.padding(contentPadding))

    }

}

