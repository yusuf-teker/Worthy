package com.yusufteker.worthy.screen.onboarding.presentation

sealed interface OnboardingAction {
    object OnGetStartedClicked : OnboardingAction
}