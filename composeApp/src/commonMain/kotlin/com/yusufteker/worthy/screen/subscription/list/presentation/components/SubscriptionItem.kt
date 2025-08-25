package com.yusufteker.worthy.screen.subscription.list.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.screen.subscription.add.presentation.components.toComposeColor
import com.yusufteker.worthy.screen.subscription.domain.model.Subscription
import com.yusufteker.worthy.screen.wishlist.list.domain.priorityColor
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.service_name

@Composable
fun SubscriptionItem(
    subscription: Subscription,
    modifier: Modifier = Modifier,
    isPreview: Boolean = false
) {

    val cardBackgroundColor = if (isPreview) {
        if (subscription.color != null) {
            subscription.color.toComposeColor()
        } else {
            AppColors.secondaryContainer
        }
    } else {
        if (true){ // TODO İS ACTIVE
            if (subscription.color != null) {
                subscription.color.toComposeColor()
            } else {
                AppColors.secondaryContainer

            }
       }else{
            Color.LightGray.copy(alpha = 0.3f)

        }
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {  },
        shape = CardDefaults.shape,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),

    )  {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Sol taraf: Icon + isim
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = subscription.icon,
                    fontSize = 28.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )


                Text(
                    text = subscription.name.ifEmpty {
                        "########"
                    },
                    style = AppTypography.titleLarge,
                    fontWeight = FontWeight.Bold
                )


                Spacer(Modifier.weight(1f))

                Text(
                    text = "${subscription.money.amount} ${subscription.money.currency.symbol}",
                    style = AppTypography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            // Sağ taraf: Fiyat + Gün
            /*Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${subscription.money.amount} ${subscription.money.currency.symbol}",
                    style = AppTypography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                subscription.scheduledDay?.let { day ->
                    Text(
                        text = "Ödeme günü: $day",
                        style = AppTypography.bodySmall,
                        color = Color.Gray
                    )
                }
            }*/
        }
    }
}
