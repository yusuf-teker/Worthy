package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun NumberPickerInput(
    modifier: Modifier = Modifier,
    label: String,
    value: Int,
    range: IntRange = 0..80,
    step: Int = 1,
    format: (Int) -> String = {     it.toString() },
    onValueChange: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldValue by remember(value) { mutableStateOf(format(value)) }

    val scrollState = rememberScrollState()
    var dropdownItemHeight by remember { mutableStateOf(0) }
    val options = remember(range, step) { range.filter { (it - range.first) % step == 0 } }
    val selectedIndex = options.indexOf(value).coerceAtLeast(0)


    LaunchedEffect(expanded) {
        if (expanded) {
            scrollState.animateScrollTo(selectedIndex * dropdownItemHeight / 4)
            scrollState.scrollTo(selectedIndex * dropdownItemHeight / 4)
        }
    }
    Column(modifier) {

        Box {

            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { input ->
                    textFieldValue = input
                    input.filter(Char::isDigit).toIntOrNull()?.let { num ->
                        if (num in range) onValueChange(num)
                    }
                },
                label = {Text(text = label) },
                modifier = modifier,
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded)
                                Icons.Default.KeyboardArrowUp
                            else
                                Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                }
            )

            // ▸ Dropdown menü
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp).onGloballyPositioned { measuredSize ->
                        dropdownItemHeight = measuredSize.size.height
                    },
                scrollState = scrollState
            ) {
                for (num in range step step) {
                    DropdownMenuItem(
                        text = { Text(format(num), style = AppTypography.bodyMedium) },
                        onClick = {
                            onValueChange(num)
                            textFieldValue = format(num)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun NumberPickerInputPreview() {
    Column(modifier = Modifier.background(AppColors.background)) {
        NumberPickerInput(
            label = "Çalışma Süresi",
            value = 40,
            range = 0..80,
            step = 5,
            onValueChange = {}
        )
    }

}
