package com.yusufteker.worthy.core.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.yusufteker.worthy.core.presentation.toRadians
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PieChart(
    values: List<Double>,
    colors: List<Color>,
    modifier: Modifier = Modifier,
    animationDuration: Int = 750,
    dividerColor: Color = Color.Black,
    dividerStrokeWidth: Float = 2f
) {
    // Negatifleri 0 yap
    val positiveValues = values.map { if (it > 0.0) it else 0.0 }
    val total = positiveValues.sum().takeIf { it > 0.0 } ?: 1.0

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

        if (positiveValues.isEmpty() || positiveValues.all { it == 0.0 }) {
            drawArc(
                color = colors.getOrElse(0) { Color.Red }, // İlk rengini kullan
                startAngle = 0f,
                sweepAngle = 360f * progress.value,
                useCenter = true
            )
        } else {
            // Dilimleri çiz
            positiveValues.forEachIndexed { i, v ->
                val sweep = 360.0f * (v / total).toFloat() * progress.value

                if (v > 0f) {
                    drawArc(
                        color = colors.getOrElse(i) { Color.Gray },
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = true
                    )
                }
                startAngle += 360f * (v / total).toFloat()
            }

            // Sınırları çiz

            val positiveCount = positiveValues.count { it > 0f }

            if (positiveCount > 1){
                startAngle = -90f


                positiveValues.forEachIndexed { i, v ->
                    val sweep = 360f * (v / total) * progress.value
                    if (sweep > 0) {
                        val startAngleRad = startAngle.toDouble().toRadians()
                        val startX = center.x + radius * cos(startAngleRad).toFloat()
                        val startY = center.y + radius * sin(startAngleRad).toFloat()

                        drawLine(
                            color = dividerColor,
                            start = center,
                            end = Offset(startX, startY),
                            strokeWidth = dividerStrokeWidth
                        )

                        if (i < positiveValues.size - 1) {
                            val endAngleRad = (startAngle + sweep).toDouble().toRadians()
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
                    startAngle += 360f * (v / total).toFloat()
                }
            }

        }
    }
}
