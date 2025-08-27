package com.yusufteker.worthy.screen.subscription.list.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusufteker.worthy.core.domain.model.AppDate
import com.yusufteker.worthy.core.domain.model.RecurringItem
import com.yusufteker.worthy.core.domain.model.isActive
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.screen.subscription.add.presentation.components.toComposeColor
import org.jetbrains.compose.ui.tooling.preview.Preview
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.service_name

@Composable
fun SubscriptionItem(
    subscription: RecurringItem.Subscription,
    modifier: Modifier = Modifier,
    onItemClicked: (RecurringItem.Subscription) -> Unit = {},
    isPreview: Boolean = false
) {
    Box(modifier = modifier.fillMaxWidth()) {

        val backgroundColor = subscription.colorHex?.toComposeColor() ?: AppColors.secondaryContainer

        val contentColor = if (backgroundColor.luminance() > 0.5f) {
            Color.Black
        } else {
            Color.White
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onItemClicked.invoke(subscription)
                },
            shape = CardDefaults.shape,
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            colors = CardDefaults.cardColors(
                containerColor = subscription.colorHex?.toComposeColor() ?: AppColors.secondaryContainer,
                contentColor = contentColor
            ),
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = subscription.icon,
                    fontSize = 28.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )

                Text(
                    text = subscription.name.ifEmpty { "########" },
                    style = AppTypography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.weight(1f))

                Text(
                    text = "${subscription.amount.amount} ${subscription.amount.currency.symbol}",
                    style = AppTypography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 🔹 Overlay - pasif aboneliklerde göster
        if (!subscription.isActive() && !isPreview) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Color.Black.copy(alpha = 0.6f),
                        shape = CardDefaults.shape
                    )
            )
        }
    }
}

@Preview
@Composable
fun SubscriptionItemPreview() {
    val subscription = RecurringItem.Subscription(
        id = 1,
        name = UiText.StringResourceId(Res.string.service_name).asString(),
        startDate = AppDate(2023, 1, 1),
        endDate = AppDate(2024, 1, 1),
        icon = "📱",
        groupId = "", //todo
    )
    SubscriptionItem(subscription = subscription, isPreview = true)
}