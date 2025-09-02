package com.yusufteker.worthy.screen.onboarding.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
import com.yusufteker.worthy.core.presentation.theme.AppDimens.ScreenPadding
import com.yusufteker.worthy.screen.onboarding.presentation.components.UserFormPager
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingScreenRoot(
    viewModel: OnboardingViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    onGetStarted: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BaseContentWrapper(
        state = state
    ) { modifier ->
        OnboardingScreen(
            modifier = modifier, state = state, onAction = { action ->
                when (action) {
                    is OnboardingAction.OnGetStartedClicked -> {
                        //viewModel.navigateTo(Routes.Dashboard)
                        onGetStarted.invoke()
                    }

                    else -> Unit
                }
                viewModel.onAction(action)
            }, contentPadding = contentPadding
        )
    }

}

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    val focusManager = LocalFocusManager.current


    Box(
        contentAlignment = Alignment.Center, modifier = modifier.clickable(
        indication = null, interactionSource = remember { MutableInteractionSource() }) {
        focusManager.clearFocus()
    }.padding(ScreenPadding)
    ) {

        UserFormPager(
            onGetStarted = { onAction(OnboardingAction.OnGetStartedClicked(it)) })

    }
}