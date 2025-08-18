package com.yusufteker.worthy.screen.onboarding.presentation

import com.yusufteker.worthy.screen.onboarding.presentation.components.UserOnboardingData

sealed interface OnboardingAction {
    data class OnGetStartedClicked(val userData: UserOnboardingData) : OnboardingAction
}