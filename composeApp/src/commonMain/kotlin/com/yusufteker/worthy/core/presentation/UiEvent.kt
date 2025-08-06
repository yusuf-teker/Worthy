package com.yusufteker.worthy.core.presentation

import com.yusufteker.worthy.app.navigation.Routes

sealed class UiEvent {
    object NavigateBack : UiEvent()
    data class NavigateTo(val route: Routes) : UiEvent()
    data class NavigateWithData<T>(val route: Routes, val data: T) : UiEvent()

    data class ShowLoading(val isLoading: Boolean) : UiEvent()

}