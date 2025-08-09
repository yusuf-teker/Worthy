package com.yusufteker.worthy.screen.addtransaction.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddTransactionScreenRoot(
    viewModel: AddTransactionViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AddTransactionScreen(state = state, onAction = viewModel::onAction, contentPadding = contentPadding)
}

@Composable
fun AddTransactionScreen(
    state: AddTransactionState,
    onAction: (action: AddTransactionAction) -> Unit,
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