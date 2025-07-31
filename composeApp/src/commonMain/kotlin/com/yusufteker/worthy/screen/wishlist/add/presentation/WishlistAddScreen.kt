package com.yusufteker.worthy.screen.wishlist.add.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.components.AppTopBar
import com.yusufteker.worthy.core.presentation.components.ImagePickerComponent
import com.yusufteker.worthy.core.presentation.components.MoneyInput
import com.yusufteker.worthy.screen.wishlist.add.presentation.components.PriorityChooser
import com.yusufteker.worthy.screen.wishlist.add.presentation.components.WishlistCategoryDropdown
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.add_new
import worthy.composeapp.generated.resources.wishlist_button_save
import worthy.composeapp.generated.resources.wishlist_checkbox_purchased
import worthy.composeapp.generated.resources.wishlist_label_note_optional
import worthy.composeapp.generated.resources.wishlist_label_price
import worthy.composeapp.generated.resources.wishlist_label_product_name

@Composable
fun WishlistAddScreenRoot(
    viewModel: WishlistAddViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    WishlistAddScreen(state = state, onAction = viewModel::onAction, contentPadding = contentPadding)
}

@Composable
fun WishlistAddScreen(
    state: WishlistAddState,
    onAction: (action: WishlistAddAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        AppTopBar(
            title = UiText.StringResourceId(Res.string.add_new).asString(),
           // onBackClick = { onAction(WishlistAddAction.OnBackClicked) }
        )
        // 1. Görsel seçimi
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
        ) {
            val size = minOf(maxWidth, maxHeight)

            ImagePickerComponent(
                selectedImage = state.imageBitmap,
                onImageSelected = { onAction(WishlistAddAction.OnImageSelected(it)) },
                modifier = Modifier.size(size)
            )
        }

        // 2. Ürün Adı
        OutlinedTextField(
            value = state.wishlistItem.name,
            onValueChange = { onAction(WishlistAddAction.OnNameChanged(it)) },
            label = { Text(UiText.StringResourceId(Res.string.wishlist_label_product_name).asString()) },
            modifier = Modifier.fillMaxWidth()
        )

        // 3. Fiyat

        MoneyInput(
            money = state.wishlistItem.price,
            onValueChange = { onAction(WishlistAddAction.OnPriceChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = UiText.StringResourceId(Res.string.wishlist_label_price, arrayOf(state.wishlistItem.price.currency.symbol)),
            //isError = state.isPriceError,
            //errorMessage = state.priceErrorMessage
        )

        // 4. Kategori
        WishlistCategoryDropdown(
            categories = state.wishlistCategories,
            selectedCategory = state.wishlistItem.category ?: state.wishlistCategories.firstOrNull(),
            onCategorySelected = {
                onAction(WishlistAddAction.OnCategorySelected(it))
            },
            onNewCategoryCreated = {
                onAction(WishlistAddAction.OnNewCategoryCreated(it))
            }
        )

        // 5. Öncelik (1-5)
        PriorityChooser(
            value = state.wishlistItem.priority,
            onValueChange = { onAction(WishlistAddAction.OnPriorityChanged(it)) }
        )

        // 6. Not
        OutlinedTextField(
            value = state.wishlistItem.note ?: "",
            onValueChange = { onAction(WishlistAddAction.OnNoteChanged(it)) },
            label = { Text(UiText.StringResourceId(Res.string.wishlist_label_note_optional).asString()) },
            modifier = Modifier.fillMaxWidth()
        )

        // 7. Satın alındı mı?
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = state.wishlistItem.isPurchased,
                onCheckedChange = { onAction(WishlistAddAction.OnPurchasedChanged(it)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(UiText.StringResourceId(Res.string.wishlist_checkbox_purchased).asString())
        }

        // 8. Kaydet Butonu
        Button(
            onClick = { onAction(WishlistAddAction.OnSaveClicked) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(UiText.StringResourceId(Res.string.wishlist_button_save).asString())
        }
    }
}