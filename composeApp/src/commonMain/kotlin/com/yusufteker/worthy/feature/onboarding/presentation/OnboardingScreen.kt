package com.yusufteker.worthy.feature.onboarding.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.core.presentation.theme.MyColors
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingScreenRoot(
    viewModel:OnboardingViewModel = koinViewModel(),
    contentPadding : PaddingValues = PaddingValues(),
    onGetStarted: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    OnboardingScreen(
        state = state,
         onAction = { action ->
            when (action) {
                else -> Unit
            }
            viewModel.onAction(action)
        },
        contentPadding = contentPadding
    )
}


@Composable
fun OnboardingScreen(
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().background(
        MyColors.background
    )){
        Text("Onboarding Screen", color = MyColors.onBackground)
    }
}