package com.yusufteker.worthy.feature.onboarding.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.components.PrimaryButton
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppDimens.ScreenPadding
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.get_started

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
                OnboardingAction.OnGetStartedClicked -> {
                    //viewModel.navigateTo(Routes.Dashboard)
                    onGetStarted.invoke()
                }

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
        AppColors.background
    ).padding(ScreenPadding)){
        Text("Onboarding Screen", color = AppColors.onBackground)
        PrimaryButton(
            text = UiText.StringResourceId(id = Res.string.get_started).asString(),

            onClick = { onAction(OnboardingAction.OnGetStartedClicked) },
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(  16.dp)
        )
    }
}