package com.yusufteker.worthy.screen.card.list.presentation
        
        import com.yusufteker.worthy.core.presentation.base.BaseState

        data class CardListState(
            override val isLoading: Boolean = false,
            val errorMessage: String? = null
        ): BaseState{
            override fun copyWithLoading(isLoading: Boolean): BaseState {
            return this.copy(isLoading = isLoading)
        }
}