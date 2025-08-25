package com.yusufteker.worthy.screen.card.list.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.screen.card.domain.repository.CardRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class CardListViewModel (
    val cardRepository: CardRepository
): BaseViewModel<CardListState>(CardListState()) {

    fun onAction(action: CardListAction) {
        when (action) {
            is CardListAction.Init -> {

            }

           is CardListAction.NavigateBack -> {
                navigateBack()
            }

            is CardListAction.AddNewCard -> {
                navigateTo(Routes.AddCard)
            }
        }
    }
    init {
        observeCardList()
    }

    fun observeCardList(){
        launchWithLoading {

            cardRepository.getAll().onEach { cards ->
                _state.update { currentState ->
                    currentState.copy(
                        cards = cards
                    )
                }
            }.launchIn(viewModelScope)

        }

    }
}