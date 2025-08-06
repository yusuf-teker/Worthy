package com.yusufteker.worthy.screen.onboarding.presentation

import com.yusufteker.worthy.core.presentation.base.BaseState

data class OnboardingState(
    val x: Int = 0,
    override val isLoading: Boolean = false
): BaseState