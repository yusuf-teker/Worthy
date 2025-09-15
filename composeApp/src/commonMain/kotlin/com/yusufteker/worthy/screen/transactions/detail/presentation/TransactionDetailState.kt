package com.yusufteker.worthy.screen.transactions.detail.presentation
        
        import com.yusufteker.worthy.core.domain.model.Category
        import com.yusufteker.worthy.core.domain.model.Transaction
        import com.yusufteker.worthy.core.presentation.base.BaseState
        import com.yusufteker.worthy.screen.card.domain.model.Card

data class TransactionDetailState(
    override val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val transaction: Transaction? = null,
    val isRefund: Boolean? = null,
    val categories: List<Category> = emptyList(),
    val selectedCategory: Category? = null,
    val cards: List<Card> = emptyList(),

    ): BaseState{
            override fun copyWithLoading(isLoading: Boolean): BaseState {
            return this.copy(isLoading = isLoading)
        }
}