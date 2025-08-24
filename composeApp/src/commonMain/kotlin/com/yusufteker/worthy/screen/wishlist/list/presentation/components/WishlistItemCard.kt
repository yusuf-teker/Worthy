package com.yusufteker.worthy.screen.wishlist.list.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.getNameResource
import com.yusufteker.worthy.core.media.loadImageBitmapFromPath
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.core.presentation.toFormattedDate
import com.yusufteker.worthy.core.presentation.util.formatted
import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistItem
import com.yusufteker.worthy.screen.wishlist.list.domain.priorityColor
import io.github.aakira.napier.Napier
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
fun WishlistItemCard(
    item: WishlistItem,
    onCheckedChange: (Boolean) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Napier.d("WishlistItemCard: ${item.imageUri}")
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = CardDefaults.shape,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, item.priorityColor)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

            LaunchedEffect(item.imageUri) {
                item.imageUri?.let { uri ->
                    Napier.d("Loading image for URI: $uri")
                    imageBitmap = loadImageBitmapFromPath(uri)
                    Napier.d("ImageBitmap result: ${imageBitmap?.let { "${it.width}x${it.height}" } ?: "null"}")
                }
            }


            imageBitmap?.let {

                androidx.compose.foundation.Image(
                    bitmap = it,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    style = AppTypography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.price.formatted(),
                    style = AppTypography.bodyMedium,
                    color = AppColors.primary
                )

                item.category?.let {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = it.getNameResource(),
                        style = AppTypography.labelSmall,
                        color = AppColors.onSurface.copy(alpha = 0.6f)
                    )
                }

                item.addedDate.let {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Eklendi: ${it.toFormattedDate()}",
                        style = AppTypography.labelSmall,
                        color = AppColors.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            PurchasedIndicator(
                isPurchased = item.isPurchased,
                onToggle = { onCheckedChange(!item.isPurchased) }
            )
        }
    }
}

@Composable
fun PurchasedIndicator(
    isPurchased: Boolean,
    onToggle: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(end = 8.dp)
            .clickable { onToggle() }
    ) {
        Icon(
            imageVector = if (isPurchased) Icons.Default.Check else Icons.Default.ShoppingCart,
            contentDescription = null,
            tint = if (isPurchased) AppColors.primary else AppColors.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = if (isPurchased) "Alındı" else "Alınmadı",
            style = AppTypography.labelSmall,
            color = AppColors.onSurfaceVariant,
        )
    }
}

@OptIn(ExperimentalTime::class)
@Preview
@Composable
fun WishlistItemCardPreview() {
    WishlistItemCard(
        item = WishlistItem(
            id = 1,
            name = "Örnek Ürün",
            price = Money(
                amount = 100.0,
                currency = Currency.TRY
            ),
            category = null,
            priority = 1,
            isPurchased = false,
            addedDate = Clock.System.now().epochSeconds,
            note = "Bu bir örnek not",
            imageUri = "https://example.com/image.jpg"
        ),
        onCheckedChange = {},
        onClick = {}
    )
}