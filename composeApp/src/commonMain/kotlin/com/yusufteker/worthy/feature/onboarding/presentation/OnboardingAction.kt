package com.yusufteker.worthy.feature.onboarding.presentation

sealed interface OnboardingAction {
    object OnGetStartedClicked : OnboardingAction
}