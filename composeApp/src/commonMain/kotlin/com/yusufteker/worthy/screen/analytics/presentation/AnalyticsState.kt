        package com.yusufteker.worthy.screen.analytics.presentation
        
        import com.yusufteker.worthy.core.presentation.base.BaseState

        data class AnalyticsState(
            override val isLoading: Boolean = false,
            val errorMessage: String? = null
        ): BaseState{
            override fun copyWithLoading(isLoading: Boolean): BaseState {
            return this.copy(isLoading = isLoading)
        }
}