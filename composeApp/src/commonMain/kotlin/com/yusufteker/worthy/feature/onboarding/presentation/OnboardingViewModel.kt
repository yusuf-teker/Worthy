package com.yusufteker.worthy.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
class OnboardingViewModel(
) : ViewModel() {


    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state

    fun onAction(action: OnboardingAction){
        when(action){
            else -> Unit
        }


    }
}