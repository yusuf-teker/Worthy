package com.yusufteker.worthy.screen.onboarding.presentation

import com.yusufteker.worthy.core.presentation.base.BaseState

data class OnboardingState(
    override val isLoading: Boolean = false
) : BaseState {
    override fun copyWithLoading(isLoading: Boolean): BaseState {
        return copy(isLoading = isLoading)
    }
}