package com.yusufteker.worthy.screen.wishlist.add.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusufteker.worthy.core.presentation.UiText
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.wishlist_label_priority

@Composable
fun PriorityChooser(
    value: Int,
    onValueChange: (Int) -> Unit,
    onCreateNewCategory: () -> Unit = {}
) {
    // Basit renk listesi
    val colors = listOf(
        Color(0xFF4CAF50), // Yeşil
        Color(0xFF8BC34A), // Açık yeşil
        Color(0xFFFFC107), // Sarı
        Color(0xFFFF9800), // Turuncu
        Color(0xFFFF5722)  // Kırmızı
    )

    // Güvenli index kontrolü
    val safeIndex = (value - 1).coerceIn(0, 4)
    val currentColor by animateColorAsState(
        targetValue = colors[safeIndex],
        animationSpec = tween(300),
        label = "color"
    )

    // Basit slider - tıklanabilir daireler
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = UiText.StringResourceId(Res.string.wishlist_label_priority).asString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = currentColor
        )
        repeat(5) { index ->
            val priority = index + 1
            val isSelected = priority == value

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 40.dp else 30.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) colors[index]
                            else colors[index].copy(alpha = 0.3f)
                        ).clickable { onValueChange(priority) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$priority",
                        color = if (isSelected) Color.White else colors[index],
                        fontSize = if (isSelected) 18.sp else 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
