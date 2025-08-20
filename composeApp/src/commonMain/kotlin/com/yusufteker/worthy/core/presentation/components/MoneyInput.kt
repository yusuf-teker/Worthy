package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.emptyMoney
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.amount

@Composable
fun MoneyInput(
    money: Money? = emptyMoney(),
    onValueChange: (Money?) -> Unit,
    modifier: Modifier = Modifier,
    label: UiText = UiText.StringResourceId(Res.string.amount),
    isError: Boolean = false,
    errorMessage: UiText? = null
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = if (money?.amount == 0.0) "" else money?.amount.toString(),
            placeholder = { Text(label.asString()) },
            onValueChange = {
                val newAmount = it.toDoubleOrNull() ?: 0.0
                onValueChange(money?.copy(amount = newAmount))
            },
            label = { Text(label.asString()) },
            isError = isError,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = modifier.fillMaxWidth(),
            trailingIcon = {
                Box {
                    TextButton(
                        onClick = { expanded = true },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        money?.currency?.symbol?.let { Text(it) }
                    }

                    DropdownMenu(
                        expanded = expanded, onDismissRequest = { expanded = false }) {
                        Currency.entries.forEach { currency ->
                            DropdownMenuItem(text = { Text("${currency.symbol} ") }, onClick = {
                                expanded = false
                                onValueChange(money?.copy(currency = currency))
                            })
                        }
                    }
                }
            },
            singleLine = true
        )
/*
        // Hata mesajı
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage.asString(),
                color = AppColors.error,
                style = AppTypography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }*/
        ErrorText(errorMessage?.asString())
    }

}
