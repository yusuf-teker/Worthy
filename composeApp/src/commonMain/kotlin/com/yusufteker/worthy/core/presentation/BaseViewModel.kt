package com.yusufteker.worthy.core.presentation


import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.app.navigation.Routes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

import org.koin.core.component.inject
import org.yusufteker.routealarm.core.presentation.popup.PopupManager

open class BaseViewModel : ViewModel(), KoinComponent {

    protected val popupManager: PopupManager by inject()

    val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    suspend fun sendUiEvent(event: UiEvent) {
        _uiEvent.emit(event)
    }

    fun sendUiEventSafe(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    fun navigateTo(route: Routes) {
        sendUiEventSafe(UiEvent.NavigateTo(route))
    }


}
