package com.yusufteker.worthy.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.yusufteker.worthy.core.presentation.UiEvent
import com.yusufteker.worthy.core.presentation.base.BaseState
import com.yusufteker.worthy.core.presentation.base.BaseViewModel

@Composable
fun <S : BaseState, VM : BaseViewModel<S>> NavigationHandler(
    viewModel: VM,
    onNavigate: (Routes, data: Any? ) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is UiEvent.NavigateTo) {
                onNavigate(event.route, null)
            }
            if (event is UiEvent.NavigateWithData<*>){
                onNavigate(event.route, event.data)

            }
        }
    }
}