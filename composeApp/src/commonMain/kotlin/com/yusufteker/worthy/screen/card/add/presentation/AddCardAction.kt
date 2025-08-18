package com.yusufteker.worthy.screen.card.add.presentation

import com.yusufteker.worthy.core.domain.model.Card

sealed interface AddCardAction {
    object Init : AddCardAction
    object OnNavigateBack : AddCardAction

    data class AddCard(val card: Card) : AddCardAction

}