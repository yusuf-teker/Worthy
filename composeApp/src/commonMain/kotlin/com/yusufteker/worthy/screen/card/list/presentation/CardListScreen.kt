package com.yusufteker.worthy.screen.card.list.presentation


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
import com.yusufteker.worthy.core.presentation.components.AppTopBar
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.add_new_card


@Composable
fun CardListScreenRoot(
    viewModel: CardListViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BaseContentWrapper(state = state) {
        CardListScreen(
            state = state,
            onAction = viewModel::onAction,
            contentPadding = contentPadding
        )
    }
}

@Composable
fun CardListScreen(
    state: CardListState,
    onAction: (action: CardListAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {

Scaffold(
    modifier = Modifier.fillMaxSize().padding(contentPadding),
    topBar = {
        AppTopBar(
            title = UiText.StringResourceId(Res.string.add_new_card).asString(),
            onNavIconClick = {}
        )
    }
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