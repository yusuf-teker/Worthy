package com.yusufteker.worthy.screen.transaction.list.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TransactionListScreenRoot(
    viewModel: TransactionListViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    TransactionListScreen(
        state = state, onAction = viewModel::onAction, contentPadding = contentPadding
    )
}

@Composable
fun TransactionListScreen(
    modifier: Modifier = Modifier,
    state: TransactionListState,
    onAction: (action: TransactionListAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // TODO
    }
}