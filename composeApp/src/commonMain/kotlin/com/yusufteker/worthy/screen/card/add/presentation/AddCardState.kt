package com.yusufteker.worthy.screen.card.add.presentation

import com.yusufteker.worthy.core.presentation.base.BaseState

data class AddCardState(
    override val isLoading: Boolean = false, val errorMessage: String? = null
) : BaseState {
    override fun copyWithLoading(isLoading: Boolean) = copy(isLoading = isLoading)

}