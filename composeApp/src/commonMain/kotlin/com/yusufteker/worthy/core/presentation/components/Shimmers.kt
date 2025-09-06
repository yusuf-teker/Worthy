package com.yusufteker.worthy.core.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.presentation.theme.AppColors

@Composable
fun ShimmerBar(
    maxHeight: Dp,
    baseFraction: Float
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmerBars")

    val animatedFraction by infiniteTransition.animateFloat(
        initialValue = baseFraction * 0.7f,
        targetValue = baseFraction,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "heightAnim"
    )

    Box(
        modifier = Modifier
            .width(28.dp)
            .height(maxHeight)
            .clip(RoundedCornerShape(4.dp))
            .background(Color.Transparent),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height((maxHeight.value * animatedFraction).dp)
                .clip(RoundedCornerShape(4.dp))
                .background(AppColors.surfaceVariant.copy(alpha = 0.5f))
        )
    }
}

@Composable
fun ShimmerLabel(width: Dp = 40.dp, height: Dp = 12.dp) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmerLabel")

    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alphaAnim"
    )

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(4.dp))
            .background(AppColors.surfaceVariant.copy(alpha = alphaAnim))
    )
}


@Composable
fun ShimmerBarsGroup(
    modifier: Modifier = Modifier,
    maxBarHeight: Dp = 120.dp
) {
    val barFractions = listOf(0.9f, 0.5f, 0.8f, 0.4f) // büyük-küçük-büyük-küçük

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        barFractions.forEachIndexed { index, fraction ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                // Shimmer bar
                ShimmerBar(maxHeight = maxBarHeight, baseFraction = fraction)
                Spacer(Modifier.height(4.dp))
                // Shimmer amount placeholder
                ShimmerLabel(width = 50.dp, height = 14.dp)
            }

            if (index != barFractions.lastIndex) {
                Spacer(Modifier.width(16.dp))
            }
        }
    }
}

