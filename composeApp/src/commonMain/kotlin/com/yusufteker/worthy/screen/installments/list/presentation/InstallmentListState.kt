package com.yusufteker.worthy.screen.installments.list.presentation
        
        import com.yusufteker.worthy.core.presentation.base.BaseState
        import com.yusufteker.worthy.screen.installments.list.domain.model.InstallmentCardUIModel

data class InstallmentListState(
            override val isLoading: Boolean = false,
            val errorMessage: String? = null,
            val installments: List<InstallmentCardUIModel> = emptyList<InstallmentCardUIModel>()
        ): BaseState{
            override fun copyWithLoading(isLoading: Boolean): BaseState {
            return this.copy(isLoading = isLoading)
        }
}