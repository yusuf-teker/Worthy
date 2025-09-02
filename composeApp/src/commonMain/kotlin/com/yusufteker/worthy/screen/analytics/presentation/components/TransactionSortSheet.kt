package com.yusufteker.worthy.screen.analytics.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.presentation.UiText
import org.jetbrains.compose.resources.StringResource
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.sort_amount_asc
import worthy.composeapp.generated.resources.sort_amount_desc
import worthy.composeapp.generated.resources.sort_date_asc
import worthy.composeapp.generated.resources.sort_date_desc

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionSortSheet(
    selectedSort: SortOption,
    onSortSelected: (SortOption) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SortOption.values().forEach { option ->
                FilterChip(
                    selected = selectedSort == option,
                    onClick = { onSortSelected(option) },
                    label = { Text(UiText.StringResourceId(option.labelRes).asString()) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

enum class SortOption {
    DATE_DESC,
    DATE_ASC,
    AMOUNT_DESC,
    AMOUNT_ASC
}

val SortOption.labelRes: StringResource
    get() = when (this) {
        SortOption.DATE_DESC -> Res.string.sort_date_desc
        SortOption.DATE_ASC -> Res.string.sort_date_asc
        SortOption.AMOUNT_DESC -> Res.string.sort_amount_desc
        SortOption.AMOUNT_ASC -> Res.string.sort_amount_asc
    }