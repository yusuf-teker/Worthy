package com.yusufteker.worthy.screen.wishlist.add.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.core.presentation.components.ImagePickerComponent
import com.yusufteker.worthy.screen.wishlist.add.presentation.components.PrioritySlider
import com.yusufteker.worthy.screen.wishlist.add.presentation.components.WishlistCategoryDropdown
import org.koin.compose.viewmodel.koinViewModel

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
        // 1. Görsel seçimi
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
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
            label = { Text("Ürün Adı") },
            modifier = Modifier.fillMaxWidth()
        )

        // 3. Fiyat
        OutlinedTextField(
            value = state.wishlistItem.price.formatted() ,
            onValueChange = { onAction(WishlistAddAction.OnPriceChanged(it.toDoubleOrNull() ?: 0.0)) },
            label = { "Fiyat (₺)" },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        // 4. Kategori
        WishlistCategoryDropdown(
            categories = state.wishlistCategories,
            selectedCategory = state.wishlistItem.category,
            onCategorySelected = { onAction(WishlistAddAction.OnCategorySelected(it)) }
        )

        // 5. Öncelik (1-5)
        PrioritySlider(
            priority = state.wishlistItem.priority,
            onPriorityChanged = { onAction(WishlistAddAction.OnPriorityChanged(it)) }
        )

        // 6. Not
        OutlinedTextField(
            value = state.wishlistItem.note ?: "",
            onValueChange = { onAction(WishlistAddAction.OnNoteChanged(it)) },
            label = { Text("Not (isteğe bağlı)") },
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
            Text("Satın alındı olarak işaretle")
        }

        // 8. Kaydet Butonu
        Button(
            onClick = { onAction(WishlistAddAction.OnSaveClicked) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Kaydet")
        }
    }
}
