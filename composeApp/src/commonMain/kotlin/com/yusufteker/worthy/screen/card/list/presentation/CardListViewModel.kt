package com.yusufteker.worthy.screen.card.list.presentation

import com.yusufteker.worthy.core.presentation.base.BaseViewModel

class CardListViewModel : BaseViewModel<CardListState>(CardListState()) {

    fun onAction(action: CardListAction) {
        when (action) {
            is CardListAction.Init -> {
                // TODO
            }
        }
    }
}