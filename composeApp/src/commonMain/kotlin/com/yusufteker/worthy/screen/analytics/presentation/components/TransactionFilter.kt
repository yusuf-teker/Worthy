package com.yusufteker.worthy.screen.analytics.presentation.components

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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.model.Card
import com.yusufteker.worthy.core.domain.model.CardBrand
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.CategoryType
import com.yusufteker.worthy.core.domain.model.getNameResource
import com.yusufteker.worthy.screen.analytics.domain.TimePeriod
import org.jetbrains.compose.ui.tooling.preview.Preview

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

    clearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isCategoryExpanded by remember { mutableStateOf(false) }
    var isCardExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(16.dp)) {

        // Tarih filtreleme
        Text(text = "Tarih", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            maxLines = 2
        ) {
            TimePeriod.entries.forEach { timePeriod ->
                val isSelected = timePeriod == selectedTimePeriod
                Button(
                    onClick = { onSelectedTimePeriodSelected(timePeriod) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = timePeriod.label.asString(),
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Kategori filtreleme
        ExpandableFilterSection(
            title = "Kategori",
            isExpanded = isCategoryExpanded,
            onToggleExpanded = { isCategoryExpanded = !isCategoryExpanded }
        ) {

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp), // minimum genişlik 100dp
                modifier = Modifier.padding(8.dp),
                contentPadding = PaddingValues(8.dp)

            ) {
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
                        Text(text = category.getNameResource())
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
                    Text(text = "${card.cardBrand?.name ?: "Kart"} • ${card.cardNumber.takeLast(4)}")
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
            Text(text = title, style = MaterialTheme.typography.titleMedium)
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
