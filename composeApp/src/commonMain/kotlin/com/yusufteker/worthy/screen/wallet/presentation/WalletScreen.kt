package com.yusufteker.worthy.screen.wallet.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WalletScreenRoot(
    viewModel: WalletViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BaseContentWrapper(
        state = state
    ) {
        WalletScreen(state = state, onAction = viewModel::onAction, contentPadding = contentPadding)
    }
}

@Composable
fun WalletScreen(
    state: WalletState,
    onAction: (action: WalletAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // TODO
        }
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

}