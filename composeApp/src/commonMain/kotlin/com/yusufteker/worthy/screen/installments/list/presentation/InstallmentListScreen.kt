package com.yusufteker.worthy.screen.installments.list.presentation


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import com.yusufteker.worthy.core.presentation.base.AppScaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
import com.yusufteker.worthy.core.presentation.components.AppTopBar
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import com.yusufteker.worthy.app.navigation.NavigationHandler
import com.yusufteker.worthy.app.navigation.NavigationModel
import com.yusufteker.worthy.screen.installments.list.presentation.component.InstallmentCard
import com.yusufteker.worthy.screen.installments.list.presentation.component.InstallmentListAccordion
import worthy.composeapp.generated.resources.installments

@Composable
fun InstallmentListScreenRoot(
    viewModel: InstallmentListViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    onNavigateTo: (NavigationModel) -> Unit,


) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.onAction(InstallmentListAction.Init)
    }

    NavigationHandler(viewModel){ model ->
        onNavigateTo(model)
    }
    
    BaseContentWrapper(state = state) { modifier ->
        InstallmentListScreen(
            state = state,
            onAction = viewModel::onAction,
            contentPadding = contentPadding,
            modifier = modifier

        )
    }
}

@Composable
fun InstallmentListScreen(
    state: InstallmentListState,
    onAction: (action: InstallmentListAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
    modifier: Modifier = Modifier

) {

AppScaffold(
    modifier = modifier.fillMaxSize().padding(contentPadding),
    topBar = {
        AppTopBar(
            title = UiText.StringResourceId(Res.string.installments).asString(),
            onNavIconClick = {
                onAction(InstallmentListAction.NavigateBack)
            }
        )
    }
){ paddingValues ->
    Column(modifier.padding(paddingValues)) {
        InstallmentListAccordion(
            installments = state.installments,
            onItemClicked = {
                //onAction(InstallmentListAction.OnInstallmentClicked(it))
            }
        )
    }

    /*LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(state.installments) { index, installment ->

            InstallmentCard(installment = installment,)
        }
    }*/
}
  
}