package com.yusufteker.worthy.screen.transactions.list.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.CategoryType
import com.yusufteker.worthy.core.domain.model.TransactionType
import com.yusufteker.worthy.core.domain.model.getNameResource
import com.yusufteker.worthy.core.domain.model.labelRes
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.screen.transactions.domain.model.TimePeriod
import com.yusufteker.worthy.screen.card.domain.model.Card
import com.yusufteker.worthy.screen.card.domain.model.CardBrand
import org.jetbrains.compose.ui.tooling.preview.Preview
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.filter_card
import worthy.composeapp.generated.resources.filter_category
import worthy.composeapp.generated.resources.filter_date
import worthy.composeapp.generated.resources.filter_none
import worthy.composeapp.generated.resources.filter_transaction_type

@Composable
fun TransactionFilter(
    categories: List<Category>, // tüm kategori listesi
    selectedCategories: List<Category> = emptyList(),
    onCategorySelected: (Category, Boolean) -> Unit, // kategori seçildi/çıkarıldı

    cards: List<Card>, // tüm kart listesi
    selectedCards: List<Card> = emptyList(),
    onCardSelected: (Card, Boolean) -> Unit,

    selectedTimePeriod: TimePeriod = TimePeriod.NONE,
    onSelectedTimePeriodSelected: (TimePeriod) -> Unit,

    selectedTransactionType: TransactionType? = null,
    onTransactionTypeSelected: (TransactionType?) -> Unit,
    clearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isCategoryExpanded by remember { mutableStateOf(true) }
    var isCardExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(16.dp)) {

        // Tarih filtreleme
        Text(
            text = UiText.StringResourceId(Res.string.filter_date).asString(),
            style = AppTypography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            maxLines = 2
        ) {
            TimePeriod.entries.forEach { timePeriod ->
                val isSelected = timePeriod == selectedTimePeriod
                FilterChip(
                    onClick = {
                        onSelectedTimePeriodSelected(timePeriod)
                    },
                    label = { Text(timePeriod.label.asString()) },
                    selected = isSelected,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AppColors.onPrimary,
                        selectedLabelColor = AppColors.primary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        selectedBorderColor = AppColors.onPrimary,
                        selectedBorderWidth = 1.dp,
                        enabled = true,
                        selected = isSelected
                    )
                )
            }
        }
        Text(
            text = UiText.StringResourceId(Res.string.filter_transaction_type).asString(),
            style = AppTypography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            maxLines = 2
        ) {
            FilterChip(
                onClick = {
                    onTransactionTypeSelected(null)
                },
                label = { Text(UiText.StringResourceId(Res.string.filter_none).asString()) },
                selected = selectedTransactionType == null,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AppColors.onPrimary,
                    selectedLabelColor = AppColors.primary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    selectedBorderColor = AppColors.onPrimary,
                    selectedBorderWidth = 1.dp,
                    enabled = true,
                    selected = selectedTransactionType == null
                )
            )
            TransactionType.entries.forEach { type ->
                val isSelected = selectedTransactionType == type
                FilterChip(
                    onClick = {
                        onTransactionTypeSelected(type)
                    },
                    label = { Text(UiText.StringResourceId(type.labelRes).asString()) },
                    selected = isSelected,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AppColors.onPrimary,
                        selectedLabelColor = AppColors.primary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        selectedBorderColor = AppColors.onPrimary,
                        selectedBorderWidth = 1.dp,
                        enabled = true,
                        selected = isSelected
                    )
                )
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Kategori filtreleme
        ExpandableFilterSection(
            title = UiText.StringResourceId(Res.string.filter_category).asString(),
            isExpanded = isCategoryExpanded,
            onToggleExpanded = { isCategoryExpanded = !isCategoryExpanded }
        ) {

            // kategori listesi
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 120.dp), // daha düzenli görünmesi için 120
                modifier = Modifier.padding(8.dp),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {

                    // "Hiçbiri" seçeneği
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .toggleable(
                                value = selectedCategories.isEmpty(),
                                onValueChange = {
                                    if (!selectedCategories.isEmpty()) {
                                        // boşalt
                                        selectedCategories.forEach { cat ->
                                            onCategorySelected(
                                                cat,
                                                false
                                            )
                                        }
                                    }
                                }
                            )
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(checked = selectedCategories.isEmpty(), onCheckedChange = null)
                        Spacer(Modifier.width(8.dp))
                        Text(text = UiText.StringResourceId(Res.string.filter_none).asString())
                    }
                }
                items(categories) { category ->
                    val checked = selectedCategories.contains(category)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .toggleable(
                                value = checked,
                                onValueChange = { onCategorySelected(category, it) }
                            )
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = checked, onCheckedChange = null)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = category.getNameResource(),
                            style = AppTypography.bodyMedium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Kart filtreleme
        ExpandableFilterSection(
            title = "Kart",
            isExpanded = isCardExpanded,
            onToggleExpanded = { isCardExpanded = !isCardExpanded }
        ) {
            cards.forEach { card ->
                val checked = selectedCards.contains(card)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .toggleable(
                            value = checked,
                            onValueChange = { onCardSelected(card, it) }
                        )
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = checked, onCheckedChange = null)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "${
                            card.cardBrand?.name ?: UiText.StringResourceId(Res.string.filter_card)
                                .asString()
                        } • ${card.cardNumber.takeLast(4)}"
                    )
                }
            }
        }
    }
}

@Composable
fun ExpandableFilterSection(
    title: String,
    isExpanded: Boolean,
    onToggleExpanded: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { onToggleExpanded() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, style = AppTypography.titleMedium)
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }
        if (isExpanded) {
            Column(modifier = Modifier.padding(start = 8.dp)) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun TransactionFilterPreview() {
    var selectedTimePeriod by remember { mutableStateOf(TimePeriod.NONE) }
    var selectedCategories by remember { mutableStateOf(listOf<Category>()) }
    var selectedCards by remember { mutableStateOf(listOf<Card>()) }
    val cards = listOf(
        Card(
            id = 1,
            cardNumber = "1234 5678 9012 3456",
            cardHolderName = "",
            expiryYear = 1,
            expiryMonth = 2,
            cvv = "321",
            cardBrand = CardBrand.Mastercard
        ),
        Card(
            id = 2,
            cardNumber = "9",
            expiryYear = 1,
            expiryMonth = 2,
            cardHolderName = "",
            cvv = "123",
            cardBrand = CardBrand.Mastercard
        )
    )
    val categories = listOf(
        Category(id = 1, name = "Yiyecek", type = CategoryType.INCOME),
        Category(id = 2, name = "Ulaşım", type = CategoryType.INCOME),
        Category(id = 3, name = "Alışveriş", type = CategoryType.INCOME),
        Category(id = 4, name = "Eğlence", type = CategoryType.INCOME)
    )

    TransactionFilter(
        categories = categories,
        selectedCategories = selectedCategories,
        selectedTimePeriod = selectedTimePeriod,
        onSelectedTimePeriodSelected = { selectedTimePeriod = it },
        onCategorySelected = { category, selected ->
            selectedCategories = if (selected) selectedCategories + category
            else selectedCategories - category
        },
        selectedTransactionType = null,
        onTransactionTypeSelected = {},
        cards = cards,
        selectedCards = selectedCards,
        onCardSelected = { card, isSelected ->
            selectedCards = if (isSelected) selectedCards + card
            else selectedCards - card
        },
        clearFilters = {
            selectedCards = emptyList()
            selectedCategories = emptyList()
            selectedTimePeriod = TimePeriod.NONE
        }
    )
}
