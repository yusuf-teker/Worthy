package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.screen.card.domain.model.Card
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.theme.AppColors
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.add_new_card
import worthy.composeapp.generated.resources.card_selector_label

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardSelector(
    cards: List<Card>?,
    selectedCard: Card? = null,
    modifier: Modifier = Modifier,
    onCardSelected: (Card) -> Unit,
    onAddNewCard: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedCard?.nickname ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(UiText.StringResourceId(Res.string.card_selector_label).asString()) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            leadingIcon = null,
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            cards?.forEach { card ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = card.nickname ?: "",
                            modifier = Modifier.padding(start = 8.dp),
                            fontWeight = FontWeight.Medium
                        )

                    },
                    leadingIcon = null,
                    onClick = {
                        onCardSelected(card)
                        expanded = false
                    }
                )
            }


            DropdownMenuItem(
                text = {
                    Text(
                        text = UiText.StringResourceId(Res.string.add_new_card).asString(),
                        fontWeight = FontWeight.SemiBold,
                        color = AppColors.primary
                    )
                },
                onClick = {
                    expanded = false
                    onAddNewCard.invoke()
                }
            )
        }
    }

}
