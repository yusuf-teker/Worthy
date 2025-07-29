package com.yusufteker.worthy.screen.wishlist.add.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PrioritySlider(
    priority: Int,
    onPriorityChanged: (Int) -> Unit
) {
    Column {
        Text("Öncelik: $priority", modifier = Modifier.padding(bottom = 4.dp))
        Slider(
            value = priority.toFloat(),
            onValueChange = { onPriorityChanged(it.toInt()) },
            valueRange = 1f..5f,
            steps = 3 // 1,2,3,4,5
        )
    }
}
