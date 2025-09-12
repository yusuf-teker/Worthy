package com.yusufteker.worthy.screen.card.list.presentation

import com.yusufteker.worthy.screen.card.domain.model.Card

sealed interface CardListAction {
    object Init : CardListAction
    object NavigateBack : CardListAction

    object AddNewCard : CardListAction

    data class onCardSelected(val card: Card) : CardListAction
    data class onDeleteCard(val card: Card?) : CardListAction



}