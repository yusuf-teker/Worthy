package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.presentation.UiText
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.day_input_hint
import worthy.composeapp.generated.resources.day_of_month
import worthy.composeapp.generated.resources.last_day

@Composable
fun DayOfMonthSelector(
    selectedDay: Int?, onDayChange: (Int?) -> Unit
) {
    var text by remember { mutableStateOf(selectedDay?.toString() ?: "") }
    var isLastDay by remember { mutableStateOf(selectedDay == null) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { input ->
                val digitsOnly = input.filter { it.isDigit() }
                val number = digitsOnly.toIntOrNull()

                if (number != null && number in 1..28) {
                    text = digitsOnly
                    isLastDay = false
                    onDayChange(number)
                } else if (digitsOnly.isEmpty()) {
                    text = ""
                    onDayChange(null)
                }
            },
            label = {
                Text(
                    UiText.StringResourceId(if (isLastDay) Res.string.last_day else Res.string.day_input_hint)
                        .asString()
                )
            },
            placeholder = {
                Text(
                    UiText.StringResourceId(if (isLastDay) Res.string.last_day else Res.string.day_of_month)
                        .asString()
                )
            },
            modifier = Modifier.weight(1f),
            enabled = !isLastDay,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isLastDay, onCheckedChange = { checked ->
                    isLastDay = checked
                    if (checked) {
                        text = ""
                        onDayChange(null) // null = ayın son günü
                    }
                })
            Text(UiText.StringResourceId(Res.string.last_day).asString())
        }
    }
}
