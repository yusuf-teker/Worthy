package com.yusufteker.worthy.screen.card.add.presentation

import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddCardViewModel : BaseViewModel() {
    private val _state = MutableStateFlow(AddCardState())
    val state: StateFlow<AddCardState> = _state

    fun onAction(action: AddCardAction) {
        when (action) {
            is AddCardAction.Init -> {
                // TODO
            }
        }
    }
}