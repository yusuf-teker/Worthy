package com.yusufteker.worthy.core.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.presentation.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.yusufteker.routealarm.core.presentation.popup.PopupManager

open class BaseViewModel<S : BaseState>(
    initialState: S
) : ViewModel(), KoinComponent {

    protected val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state

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

    fun navigateBack(){
        sendUiEventSafe(UiEvent.NavigateTo(Routes.Back))
    }

    fun <T>navigateWithData(route: Routes, data: T) {
        sendUiEventSafe(UiEvent.NavigateWithData(route, data))
    }

    private var activeLoadingJobs = 0

    protected fun launchWithLoading(block: suspend () -> Unit) {
        viewModelScope.launch {
            incrementLoading()
            try {
                block()
            } finally {
                decrementLoading()
            }
        }
    }

    private fun incrementLoading() {
        activeLoadingJobs++
        setLoading(true)
    }

    private fun decrementLoading() {
        activeLoadingJobs--
        if (activeLoadingJobs <= 0) {
            activeLoadingJobs = 0
            setLoading(false)
        }
    }

    private fun setLoading(loading: Boolean) {
        _state.update { it.copyWithLoading(loading) as S }
    }

    private fun setError(message: String) {
        //_state.update { current -> current.copyWithError(message) }
    }

}