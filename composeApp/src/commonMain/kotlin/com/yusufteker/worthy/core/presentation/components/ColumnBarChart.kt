package com.yusufteker.worthy.core.presentation.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.getMonthName
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import org.jetbrains.compose.ui.tooling.preview.Preview
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.add_recurring_item
import worthy.composeapp.generated.resources.add_transaction
import worthy.composeapp.generated.resources.add_wish
import worthy.composeapp.generated.resources.no_data
@Composable
fun ColumnBarChart(
    values: List<Float>, // 0f–1f oranlar
    labels: List<String>,
    modifier: Modifier = Modifier,
    selectedIndex: Int?,
    onBarClick: (Int) -> Unit,
    onAddWishlistClicked: () -> Unit = {},
    onAddRecurringClicked: () -> Unit= {},
    onAddTransactionClicked: () -> Unit = {}
) {
    val hasData = values.any { it > 0f }

    // Crossfade kullanarak iki state arasında geçiş
    Crossfade(
        targetState = hasData,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ), modifier = Modifier.height(120.dp)
    ) { showChart ->
        if (showChart) {
            // Chart gösterimi
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
                    LaunchedEffect(v) {
                        animatedHeight.animateTo(
                            targetHeight.value,
                            animationSpec = tween(
                                durationMillis = 800,
                                easing = FastOutSlowInEasing
                            )
                        )
                    }

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
                                .height(maxBarHeight)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Transparent),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(animatedHeight.value.dp)
                                    .clickable { onBarClick.invoke(i) }
                                    .clip(RoundedCornerShape(4.dp))
                                    .then(
                                        if (isSelected) Modifier.shadow(6.dp, RoundedCornerShape(4.dp)) else Modifier
                                    )
                                    .background(barColor)
                                    .align(Alignment.BottomCenter)
                            )
                            if (animatedHeight.value <= 0) {
                                IconButton(
                                    onClick = { onBarClick.invoke(i) }
                                ) {
                                    Icon(Icons.Default.Warning, contentDescription = null)
                                }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = labels[i],
                            style = AppTypography.labelSmall,
                            color = AppColors.onSurfaceVariant,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2
                        )
                    }
                }
            }
        } else {
            // No data gösterimi
            Box(
                modifier = modifier.fillMaxWidth().height(120.dp),
                contentAlignment = Alignment.Center
            ) {


                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = AppColors.onSurfaceVariant,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = UiText.StringResourceId(Res.string.no_data).asString(),
                            style = AppTypography.bodyMedium,
                            color = AppColors.onSurfaceVariant
                        )
                    }
                    FlowRow  (
                        maxItemsInEachRow = 2,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                            Text(
                                modifier = Modifier.clickable{
                                      onAddWishlistClicked.invoke()
                                },
                                text = "+ " + UiText.StringResourceId(Res.string.add_wish).asString(),
                                style = AppTypography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = AppColors.primary
                            )

                        Text(
                            modifier = Modifier.clickable{
                                onAddTransactionClicked.invoke()
                            },
                            text = "+ " + UiText.StringResourceId(Res.string.add_transaction).asString(),
                            style = AppTypography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = AppColors.primary
                        )
                            Text(
                                modifier = Modifier.clickable{
                                    onAddRecurringClicked.invoke()
                                },
                                text = "+ " + UiText.StringResourceId(Res.string.add_recurring_item).asString(),
                                style = AppTypography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = AppColors.primary
                            )




                    }


                }
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


fun adjustValuesForBarChart(values: List<Double?> ): List<Float>{
    return values.map { value ->
        if (value == null )
            0f
        else{
            if (value.toFloat() > 0.0f){
                (value.toFloat() * 2 / 10) + 0.2f
            }else{
                0f
            }
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
            onBarClick = {},

        )
    }

}