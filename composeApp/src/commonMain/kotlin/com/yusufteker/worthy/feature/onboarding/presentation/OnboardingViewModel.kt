package com.yusufteker.worthy.feature.onboarding.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.core.presentation.BaseViewModel
import com.yusufteker.worthy.feature.onboarding.domain.OnboardingManager
import com.yusufteker.worthy.feature.settings.domain.UserPrefsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val onboardingManager: OnboardingManager
) : BaseViewModel() {


    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state

    fun onAction(action: OnboardingAction){
        when(action){
            OnboardingAction.OnGetStartedClicked -> {
                viewModelScope.launch {
                    onboardingManager.setOnboardingCompleted(true)
                }
            }
        }


    }
}