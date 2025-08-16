package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.model.Card
import com.yusufteker.worthy.core.presentation.UiText
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.add_new_category
import worthy.composeapp.generated.resources.category_label

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardSelector(
    cards: List<Card>,
    selectedCard: Card? = null,
    modifier: Modifier = Modifier,
    onCardSelected: (Card) -> Unit,
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
            label = { Text(UiText.StringResourceId(Res.string.category_label).asString()) },
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
            cards.forEach { card ->
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
                        text = UiText.StringResourceId(Res.string.add_new_category).asString(),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                onClick = {
                    expanded = false
                }
            )
        }
    }

}
