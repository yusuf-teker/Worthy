package com.yusufteker.worthy.screen.card.add.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.screen.card.add.domain.CardRepository
import kotlinx.coroutines.launch

class AddCardViewModel(
    private val cardRepository: CardRepository
) : BaseViewModel<AddCardState>(AddCardState()) {

    fun onAction(action: AddCardAction) {
        when (action) {
            is AddCardAction.Init -> {
                // TODO
            }

            AddCardAction.OnNavigateBack -> {
                navigateBack()
            }

            is AddCardAction.AddCard -> {
                launchWithLoading {
                    cardRepository.addCard(action.card)
                    navigateBack()
                }


            }
        }
    }
}

