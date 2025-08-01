package com.yusufteker.worthy.screen.wishlist.list.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.toUri
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppColors.priorityColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import com.yusufteker.worthy.core.presentation.toFormattedDate
import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistItem
import com.yusufteker.worthy.screen.wishlist.list.domain.priorityColor
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
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

            Image(
                painter = rememberAsyncImagePainter(item.imageUri?.toUri()),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

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
                        text = it.name,
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

            Checkbox(
                checked = item.isPurchased,
                onCheckedChange = onCheckedChange
            )
        }
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
@Composable
fun SimpleKMPImageComponent(
    imageUrl: String?,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    placeholderRes: DrawableResource? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(AppColors.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                LoadingContent()
            }

            !imageUrl.isNullOrEmpty() -> {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = contentDescription,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            else -> {
                PlaceholderContent(placeholderRes)
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = AppColors.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Resim yükleniyor...",
            style = AppTypography.bodyMedium,
            color = AppColors.onSurfaceVariant
        )
    }
}

@Composable
private fun PlaceholderContent(
    placeholderRes: DrawableResource? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (placeholderRes != null) {
            Image(
                painter = painterResource(placeholderRes),
                contentDescription = "Placeholder",
                modifier = Modifier.size(64.dp),
                alpha = 0.6f
            )
        } else {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = "Placeholder",
                modifier = Modifier.size(64.dp),
                tint = AppColors.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Resim bulunamadı",
            style = AppTypography.bodyMedium,
            color = AppColors.onSurfaceVariant
        )
    }
}