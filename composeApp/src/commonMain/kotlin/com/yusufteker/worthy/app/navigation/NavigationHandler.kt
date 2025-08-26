package com.yusufteker.worthy.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.yusufteker.worthy.core.presentation.UiEvent
import com.yusufteker.worthy.core.presentation.base.BaseState
import com.yusufteker.worthy.core.presentation.base.BaseViewModel

@Composable
fun <S : BaseState, VM : BaseViewModel<S>> NavigationHandler(
    viewModel: VM,
    onNavigate: (NavigationModel) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.NavigateTo -> {
                    onNavigate(event.toModel())
                }
                is UiEvent.NavigateWithData<*> -> {
                    onNavigate(
                        event.toModel()
                    )
                }
                else -> Unit
            }
        }
    }
}


data class NavigationModel(
    val route: Routes,
    val data: Any? = null,
    val popUpToRoute: Routes? = null,
    val inclusive: Boolean = false,
    val isBack: Boolean = false // Back stack pop için
)