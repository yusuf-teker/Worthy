package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.getCurrentYear
import com.yusufteker.worthy.core.presentation.UiText
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.month
import worthy.composeapp.generated.resources.start_date_shortened
import worthy.composeapp.generated.resources.year

@Composable
fun DateSelector(
    title: String = UiText.StringResourceId(Res.string.start_date_shortened).asString(),
    month: Int?,
    onMonthChanged: (Int) -> Unit,
    year: Int?,
    onYearChanged: (Int) -> Unit,
    months: List<Int> = (1..12).toList(),
    years: List<Int> = (getCurrentYear() -10..getCurrentYear()+10).toList()
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = title, modifier = Modifier.weight(1f))

        DropdownMenuSelector(
            label = UiText.StringResourceId(Res.string.month).asString(),
            items = months,
            selectedIndex = month?.let { month.minus(1) }, // varsayalım aylar 1-12 arası
            onItemSelected = { onMonthChanged(it + 1) },
            modifier = Modifier.width(80.dp)
        )

        DropdownMenuSelector(
            label = UiText.StringResourceId(Res.string.year).asString(),
            items = years,
            selectedIndex = year?.let { years.indexOf(year) },
            onItemSelected = { onYearChanged(years[it]) },
            modifier = Modifier.width(100.dp)
        )
    }
}

@Composable
fun DropdownMenuSelector(
    label: String = "",
    items: List<Int>,
    selectedIndex: Int?,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier.wrapContentWidth()
) {

    DropdownOutlinedTextField(
        items = items.map { it.toString() }, // Int değerleri String'e çeviriyoruz
        selectedIndex = selectedIndex,
        onItemSelected = onItemSelected,
        label = label,
        modifier = modifier
    )

}



