package com.yusufteker.worthy.screen.transactions.list.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.components.BorderTransparentButton
import com.yusufteker.worthy.core.presentation.theme.AppColors
import org.jetbrains.compose.resources.painterResource
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.filter
import worthy.composeapp.generated.resources.ic_filter
import worthy.composeapp.generated.resources.ic_sort
import worthy.composeapp.generated.resources.sort

/**
 * Scroll-aware visibility için helper.
 * Aşağı kaydırıldığında gizler, yukarı kaydırıldığında gösterir.
 */
@Composable
fun rememberScrollAwareVisibility(
    lazyListState: LazyListState? = null, scrollState: ScrollState? = null
): State<Boolean> {
    var previousOffset by remember { mutableStateOf(0) }
    val isVisible = remember { mutableStateOf(true) }

    LaunchedEffect(lazyListState, scrollState) {
        snapshotFlow {
            lazyListState?.firstVisibleItemScrollOffset ?: scrollState?.value ?: 0
        }.collect { currentOffset ->
            isVisible.value = currentOffset <= previousOffset
            previousOffset = currentOffset
        }
    }

    return isVisible
}

@Composable
fun ScrollAwareButtons(
    lazyListState: LazyListState? = null,
    scrollState: ScrollState? = null,
    onSortClick: () -> Unit,
    onFilterClick: () -> Unit
) {
    val isVisible by rememberScrollAwareVisibility(lazyListState, scrollState)

    AnimatedVisibility(
        visible = isVisible, enter = expandVertically(
            animationSpec = tween(600)
        ) + fadeIn(
            animationSpec = tween(600)
        ), exit = shrinkVertically(
            animationSpec = tween(600)
        ) + fadeOut(
            animationSpec = tween(600)
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()
        ) {
            BorderTransparentButton(
                onClick = onSortClick,
                text = UiText.StringResourceId(Res.string.sort).asString(),
                leadingIcon = {
                    Icon(
                        painter = painterResource(resource = Res.drawable.ic_sort),
                        contentDescription = UiText.StringResourceId(Res.string.sort).asString(),
                        tint = AppColors.onBackground
                    )
                })
            Spacer(modifier = Modifier.width(24.dp))
            BorderTransparentButton(
                onClick = onFilterClick,
                text = UiText.StringResourceId(Res.string.filter).asString(),
                leadingIcon = {
                    Icon(
                        painter = painterResource(resource = Res.drawable.ic_filter),
                        contentDescription = UiText.StringResourceId(Res.string.filter).asString(),
                        tint = AppColors.onBackground
                    )
                })
        }
    }

}

