package com.yusufteker.worthy.di

import com.yusufteker.worthy.core.presentation.BaseViewModel
import com.yusufteker.worthy.feature.dashboard.presentation.DashboardViewModel
import com.yusufteker.worthy.feature.onboarding.domain.OnboardingManager
import com.yusufteker.worthy.feature.onboarding.presentation.OnboardingViewModel
import com.yusufteker.worthy.feature.settings.domain.UserPrefsManager
import com.yusufteker.worthy.feature.settings.presentation.SettingsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.yusufteker.routealarm.core.presentation.popup.PopupManager

expect val platformModule: Module

val sharedModule = module {

    single { PopupManager() }
    single { UserPrefsManager(get()) }
    single { OnboardingManager(get()) }

    viewModel { OnboardingViewModel(get()) }
    viewModel { BaseViewModel() }
    viewModel { DashboardViewModel() }
    viewModel { SettingsViewModel(get()) }
}