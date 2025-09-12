package com.yusufteker.worthy.screen.card.list.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.screen.card.domain.repository.CardRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.cancel
import worthy.composeapp.generated.resources.delete_card_message
import worthy.composeapp.generated.resources.delete_card_title
import worthy.composeapp.generated.resources.delete_subscription_confirm

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

            is CardListAction.onCardSelected -> {

                _state.update { currentState ->
                    currentState.copy(
                        selectedCard = action.card
                    )
                }
            }
            is CardListAction.onDeleteCard -> {
                popupManager.showConfirm(
                    title = UiText.StringResourceId(Res.string.delete_card_title, arrayOf(state.value.selectedCard?.nickname ?: "")),
                    message = UiText.StringResourceId(Res.string.delete_card_message, arrayOf(state.value.selectedCard?.nickname ?: "")),
                    onConfirm = {
                        launchWithLoading {
                            action.card?.let { card ->
                                cardRepository.deleteCard(card.id)
                                _state.update { currentState ->
                                    currentState.copy(
                                        selectedCard = null
                                    )
                                }
                            }
                        }
                    },
                    confirmLabel =  UiText.StringResourceId(Res.string.delete_subscription_confirm),
                    dismissLabel = UiText.StringResourceId(Res.string.cancel)
                )

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