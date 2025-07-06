package com.yusufteker.worthy.core.presentation.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import com.yusufteker.worthy.core.presentation.toRadians
import kotlin.math.cos
import kotlin.math.sin

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Size
import kotlin.math.min


@Composable
fun PieChart2(
    values: List<Float>,
    colors: List<Color>,
    modifier: Modifier = Modifier,
    animationDuration: Int = 750,
    dividerColor: Color = Color.Black,
    dividerStrokeWidth: Float = 2f

) {
    val total = values.sum().takeIf { it > 0f } ?: 1f

    val progress = remember { Animatable(0f) }

    LaunchedEffect(values) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = animationDuration)
        )
    }

    Canvas(modifier) {
        var startAngle = -90f

        if (values.isEmpty() || values.all { it == 0f }) {
            drawArc(
                color = Color.Red,
                startAngle = 0f,
                sweepAngle = 360f * progress.value,
                useCenter = true
            )
        } else {
            values.forEachIndexed { i, v ->
                val sweep = 360f * (v / total) * progress.value
                drawArc(
                    color = colors.getOrElse(i) { Color.Gray },
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = true
                )
                startAngle += 360f * (v / total)
            }
        }
    }
}

@Composable
fun PieChart(
    values: List<Float>,
    colors: List<Color>,
    modifier: Modifier = Modifier,
    animationDuration: Int = 750,
    dividerColor: Color = Color.Black,
    dividerStrokeWidth: Float = 2f
) {
    val total = values.sum().takeIf { it > 0f } ?: 1f
    val progress = remember { Animatable(0f) }

    LaunchedEffect(values) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = animationDuration)
        )
    }

    Canvas(modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = minOf(size.width, size.height) / 2f
        var startAngle = -90f

        if (values.isEmpty() || values.all { it == 0f }) {
            drawArc(
                color = Color.Red,
                startAngle = 0f,
                sweepAngle = 360f * progress.value,
                useCenter = true
            )
        } else {
            // Dilimleri çiz
            values.forEachIndexed { i, v ->
                val sweep = 360f * (v / total) * progress.value
                drawArc(
                    color = colors.getOrElse(i) { Color.Gray },
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = true
                )
                startAngle += 360f * (v / total)
            }

            // Sınırları çiz
            startAngle = -90f
            values.forEachIndexed { i, v ->
                val sweep = 360f * (v / total) * progress.value

                // Sadece sweep > 0 olan dilimlerin sınırlarını çiz
                if (sweep > 0) {
                    // Başlangıç çizgisi
                    val startAngleRad =(startAngle.toDouble()).toRadians()
                    val startX = center.x + radius * cos(startAngleRad).toFloat()
                    val startY = center.y + radius * sin(startAngleRad).toFloat()

                    drawLine(
                        color = dividerColor,
                        start = center,
                        end = Offset(startX, startY),
                        strokeWidth = dividerStrokeWidth
                    )

                    // Eğer son dilim değilse, bitiş çizgisini de çiz
                    if (i < values.size - 1) {
                        val endAngleRad = ((startAngle + sweep).toDouble()).toRadians()
                        val endX = center.x + radius * cos(endAngleRad).toFloat()
                        val endY = center.y + radius * sin(endAngleRad).toFloat()

                        drawLine(
                            color = dividerColor,
                            start = center,
                            end = Offset(endX, endY),
                            strokeWidth = dividerStrokeWidth
                        )
                    }
                }

                startAngle += 360f * (v / total)
            }
        }
    }
}
