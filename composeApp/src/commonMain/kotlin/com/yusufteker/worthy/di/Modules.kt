package com.yusufteker.worthy.di

import com.yusufteker.worthy.feature.onboarding.presentation.OnboardingViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {

    viewModel { OnboardingViewModel() }
}