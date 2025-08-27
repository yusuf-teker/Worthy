package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.emptyMoney
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.formatTwoDecimals
import com.yusufteker.worthy.core.presentation.util.MoneyVisualTransformation
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.amount

@Composable
fun MoneyInput( // TODO KÜSÜRAT KISMINDA EKSIKLIKLER VAR
    money: Money? = emptyMoney(),
    onValueChange: (Money?) -> Unit,
    modifier: Modifier = Modifier,
    label: UiText = UiText.StringResourceId(Res.string.amount),
    isError: Boolean = false,
    errorMessage: UiText? = null
) {


    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    Column(modifier = modifier) {
        OutlinedTextField(
            value = if (money?.amount == 0.00) "" else money?.amount?.formatTwoDecimals().toString() ,
            placeholder = { Text(label.asString()) },
            onValueChange = {
                if (it.length > 2 && !isThirdOrFourthFromEndDotOrComma(it))
                    return@OutlinedTextField
                var newAmount = it.toDoubleOrNull() ?: 0.0
                if (countDecimalPlacesLocalized(it) < 3 ){
                    onValueChange(money?.copy(amount = newAmount))
                }else{
                    newAmount = (newAmount * 100).toInt() / 100.0
                    onValueChange(money?.copy(amount = newAmount))

                }
            },
            visualTransformation = MoneyVisualTransformation(),

            label = { Text(label.asString()) },
            isError = isError,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
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
        MessageText(errorMessage?.let {  UiMessage.Error(it.asString()) })
    }

}
fun isThirdOrFourthFromEndDotOrComma(s: String): Boolean {
    if (s.length < 4) return false
    val second = s[s.length - 2]
    val third = s[s.length - 3]
    val fourth = s[s.length - 4]
    return third == '.' || third == ',' || fourth == '.' || fourth == ',' || second == '.' || second == ','
}

fun countDecimalPlacesLocalized(value: String): Int {
    val separatorIndex = maxOf(value.indexOf('.'), value.indexOf(',')) // hem nokta hem virgül kontrolü
    return if (separatorIndex < 0) 0
    else value.length - separatorIndex - 1
}