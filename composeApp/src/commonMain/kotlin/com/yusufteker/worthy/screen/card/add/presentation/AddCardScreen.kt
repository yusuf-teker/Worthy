package com.yusufteker.worthy.screen.card.add.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddCardScreenRoot(
    viewModel: AddCardViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AddCardScreen(state = state, onAction = viewModel::onAction, contentPadding = contentPadding)
}

@Composable
fun AddCardScreen(
    state: AddCardState,
    onAction: (action: AddCardAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {

    BaseContentWrapper(
        state = state
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // TODO
        }
    }

}