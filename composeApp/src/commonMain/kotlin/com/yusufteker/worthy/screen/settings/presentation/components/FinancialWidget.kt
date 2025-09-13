package com.yusufteker.worthy.screen.settings.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.core.presentation.util.formatMoneyText
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.amount_with_currency

@Composable
fun FinancialWidget(
    title: String,
    totalAmount: Double,
    color: Color,
    onClick: () -> Unit,
    currencySymbol: String = "₺"
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    style = AppTypography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = UiText.StringResourceId(
                        id = Res.string.amount_with_currency,
                        arrayOf(totalAmount.formatMoneyText(), currencySymbol)
                    ).asString(),
                    style = AppTypography.bodyMedium,
                    color = color,
                    fontWeight = FontWeight.Bold
                )
            }
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Düzenle",
                tint = color
            )
        }
    }
}
