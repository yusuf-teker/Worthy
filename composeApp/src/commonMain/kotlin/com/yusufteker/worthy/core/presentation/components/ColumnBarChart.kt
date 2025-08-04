package com.yusufteker.worthy.core.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.model.DashboardMonthlyAmount
import com.yusufteker.worthy.core.domain.model.MonthlyAmount
import com.yusufteker.worthy.core.domain.model.sumWithoutCurrencyConverted
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.getMonthName
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import org.jetbrains.compose.ui.tooling.preview.Preview
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.no_data

@Composable
fun ColumnBarChart(
    values: List<Float>, // 0f–1f oranlar
    labels: List<String>,
    modifier: Modifier = Modifier,
    selectedIndex: Int?,
    onBarClick: (Int) -> Unit
) {


    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(16.dp),


    ) {
        val maxBarHeight = 120.dp * values.max()
        values.forEachIndexed { i, v ->
            val isSelected = selectedIndex == i

            val targetHeight = 120.dp * v
            val animatedHeight = remember { Animatable(0f) }
            LaunchedEffect(Unit) {
                animatedHeight.animateTo(
                    targetHeight.value,
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = FastOutSlowInEasing
                    )
                )
            }


            // 🔑 Animasyonlu renk
            val barColor by animateColorAsState(
                targetValue = if (isSelected) AppColors.primary else AppColors.surfaceVariant,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                ), label = "barColor"
            )


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f).align(Alignment.Top)
                ) {
                Box(
                    modifier = Modifier
                        .height(maxBarHeight) // sabit yükseklik

                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Transparent),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(animatedHeight.value.dp)
                            .clickable{
                                onBarClick.invoke(i)
                            }
                            .clip(RoundedCornerShape(4.dp))
                            .then(
                                if (isSelected) Modifier.shadow(6.dp, RoundedCornerShape(4.dp)) else Modifier
                            )
                            .background(barColor)
                            .align(Alignment.BottomCenter)
                    )
                }
                Spacer(Modifier.height(8.dp))
               Text(
                    text = labels[i],
                    style = AppTypography.labelSmall,
                    color = AppColors.onSurfaceVariant ,
                   overflow = TextOverflow.Ellipsis,
                   maxLines = 2
                )
            }
        }
    }
}



@Composable
fun MiniBarChart(
    values: List<Float?>, // 0f–1f oranlar
    labels: List<Int>, // Ay isimleri
    modifier: Modifier = Modifier,
    barColor: Color = AppColors.secondary
) {
    val maxBarHeight = 60.dp

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {

        if (values.isNotEmpty()){
            values.forEachIndexed { idx, value ->
                val heightRatio = value?.coerceIn(0f, 1f) ?: 0f
                val animatedHeight = remember { Animatable(0f) }

                LaunchedEffect(value) {
                    animatedHeight.animateTo(
                        targetValue = (maxBarHeight * heightRatio).value,
                        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(16.dp)
                            .height(maxBarHeight)
                            .clip(RoundedCornerShape(2.dp)),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(animatedHeight.value.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(barColor)
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = getMonthName(labels.get(idx)).asString(),
                        style = AppTypography.labelSmall,
                        color = AppColors.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }else { // Values is Empty
            Text(
                text = UiText.StringResourceId(Res.string.no_data).asString()
            )
        }

    }
}


fun adjustValuesForBarChart(values: List<Float> ): List<Float>{
    return values.map { value ->
        if (value > 0f){
            (value * 2 / 10f) + 0.2f
        }else{
            0f
        }
    }
}


@Preview
@Composable
fun ColumnBarChartPreview(){
    Column(Modifier.background(AppColors.background)) {
        ColumnBarChart(
            values = listOf(0.25f, 0.5f, 0.75f),
            labels = listOf("Label 1", "Label 2", "Label 3"),
            selectedIndex = 1,
            onBarClick = {}
        )
    }

}