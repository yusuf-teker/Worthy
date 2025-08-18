package com.yusufteker.worthy.screen.card.add.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.core.presentation.UiEvent
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.screen.card.add.domain.CardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddCardViewModel(
    private val cardRepository: CardRepository
) : BaseViewModel() {
    private val _state = MutableStateFlow(AddCardState())
    val state: StateFlow<AddCardState> = _state

    fun onAction(action: AddCardAction) {
        when (action) {
            is AddCardAction.Init -> {
                // TODO
            }

            AddCardAction.OnNavigateBack -> {
                sendUiEventSafe(UiEvent.NavigateBack)
            }

            is AddCardAction.AddCard -> {
                viewModelScope.launch {
                    cardRepository.addCard(action.card)
                }

            }
        }
    }
}