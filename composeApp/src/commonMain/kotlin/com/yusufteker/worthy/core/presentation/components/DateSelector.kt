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

    @Composable
    fun DateSelector(
        title: String = "Geçerlilik Tarihi: ",
        month: Int?,
        onMonthChanged: (Int) -> Unit,
        year: Int?,
        onYearChanged: (Int) -> Unit,
        months: List<Int>,
        years: List<Int>
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = title, modifier = Modifier.weight(1f))

            DropdownMenuSelector(
                label = "Ay",
                items = months,
                selectedIndex = month?.let { month.minus(1) }  , // varsayalım aylar 1-12 arası
                onItemSelected = { onMonthChanged(it + 1) },
                modifier = Modifier.width(80.dp)
            )

            DropdownMenuSelector(
                label = "Yıl",
                items = years,
                selectedIndex = year?.let {  years.indexOf(year)},
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



