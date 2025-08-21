package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.yusufteker.worthy.core.presentation.theme.AppColors
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.painterResource
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.ic_calendar
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Composable
fun WheelDatePicker(
    initialDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    onDateSelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
    showBorder: Boolean = true,
    showIcon: Boolean = true,
    errorMessage: String? = null,
    title: String? = null,
) {
    var selectedYear by remember { mutableStateOf(initialDate.year) }
    var selectedMonth by remember { mutableStateOf(initialDate.monthNumber) }
    var selectedDay by remember { mutableStateOf(initialDate.dayOfMonth) }

    val daysInMonth = remember(selectedYear, selectedMonth) {
        monthLength(selectedYear, selectedMonth)
    }

    fun emitEpochSeconds() {
        val date = LocalDate(selectedYear, Month(selectedMonth), selectedDay)
        val instant = date.atStartOfDayIn(TimeZone.currentSystemDefault())
        onDateSelected(instant.toEpochMilliseconds())
    }

    val border = if (showBorder) {
        BorderStroke(
            1.dp, color = if (errorMessage != null) AppColors.error else AppColors.outline
        )
    } else {
        BorderStroke(0.dp, Color.Transparent)
    }

    Box(
        modifier = modifier // içerik boşluğu
    ) {

        Row(
            modifier = modifier.fillMaxWidth()

                .border(border, shape = RoundedCornerShape(4.dp)).height(56.dp)
                .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 4.dp)
                .background(Color.Transparent), verticalAlignment = Alignment.CenterVertically
        ) {

            if (showIcon) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(Res.drawable.ic_calendar),
                    contentDescription = "Takvim",
                    tint = AppColors.primary,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

            }

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                WheelPicker(
                    items = (1..daysInMonth).map { it.toString().padStart(2, '0') },
                    startIndex = selectedDay - 1,
                    onSelected = {
                        selectedDay = it + 1
                        emitEpochSeconds()
                    },
                    modifier = Modifier.width(30.dp)
                )

                WheelPicker(
                    items = (1..12).map { it.toString().padStart(2, '0') },
                    startIndex = selectedMonth - 1,
                    onSelected = {
                        selectedMonth = it + 1
                        if (selectedDay > monthLength(selectedYear, selectedMonth)) {
                            selectedDay = monthLength(selectedYear, selectedMonth)
                        }
                        emitEpochSeconds()
                    },
                    modifier = Modifier.width(30.dp)
                )

                WheelPicker(
                    items = (1970..2100).map { it.toString() },
                    startIndex = selectedYear - 1970,
                    onSelected = {
                        selectedYear = it + 1970
                        if (selectedDay > monthLength(selectedYear, selectedMonth)) {
                            selectedDay = monthLength(selectedYear, selectedMonth)
                        }
                        emitEpochSeconds()
                    },
                    modifier = Modifier.width(60.dp)
                )
            }
        }

        // Title → border’ın üst sol köşesinde
        if (title != null) {
            Box(
                modifier = Modifier.align(Alignment.TopStart).zIndex(2f)
                    .offset(x = 8.dp, y = (-8).dp)
            ) {
                Text(
                    text = title,

                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        color = if (errorMessage != null) AppColors.error else AppColors.onSurfaceVariant
                    ),
                    modifier = Modifier.background(AppColors.background)
                        .padding(horizontal = 2.dp) // yazının sağ-solunda boşluk
                )
            }
        }
        if (errorMessage != null) {
            Text(
                text = errorMessage, color = MaterialTheme.colorScheme.error
            )
        }

    }

}

@Composable
fun WheelPicker(
    items: List<String>,
    startIndex: Int = 0,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState(startIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    LaunchedEffect(listState.firstVisibleItemIndex) {
        onSelected(listState.firstVisibleItemIndex)
    }

    LazyColumn(
        state = listState,
        flingBehavior = flingBehavior,
        modifier = modifier.height(40.dp), // sadece 1 satır görünsün
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(items) { index, item ->
            Box(
                modifier = Modifier.fillMaxWidth().height(40.dp).background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

private fun monthLength(year: Int, month: Int): Int {
    return when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> 0
    }
}

private fun isLeapYear(year: Int) = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)


