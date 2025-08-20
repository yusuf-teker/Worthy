package com.yusufteker.worthy.screen.card.list.presentation

sealed interface CardListAction {
    object Init : CardListAction
    object NavigateBack : CardListAction

    object AddNewCard : CardListAction


}