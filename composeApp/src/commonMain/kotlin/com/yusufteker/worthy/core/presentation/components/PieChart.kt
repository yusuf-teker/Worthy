package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun PieChart(
    values: List<Float>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val total = values.sum().takeIf { it > 0f } ?: 1f
    Canvas(modifier) {
        var startAngle = -90f
        if (values.isEmpty() || values.all { it == 0f }){
            drawArc(
                color = Color.Red ,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = true
            )
        }else{
            values.forEachIndexed { i, v ->
                val sweep = 360f * (v / total)
                drawArc(
                    color = colors.getOrElse(i) { Color.Gray },
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = true
                )
                startAngle += sweep
            }
        }

    }
}