package com.yusufteker.worthy.screen.wishlist.add.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.app.navigation.NavigationHandler
import com.yusufteker.worthy.app.navigation.NavigationModel
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.domain.model.CategoryType
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
import com.yusufteker.worthy.core.presentation.components.AppButton
import com.yusufteker.worthy.core.presentation.components.AppTopBar
import com.yusufteker.worthy.core.presentation.components.CategorySelector
import com.yusufteker.worthy.core.presentation.components.ImagePickerComponent
import com.yusufteker.worthy.core.presentation.components.MoneyInput
import com.yusufteker.worthy.screen.wishlist.add.presentation.components.PriorityChooser
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
    contentPadding: PaddingValues = PaddingValues(),
    onNavigateTo: (NavigationModel) -> Unit = {}

    ) {

    NavigationHandler(viewModel){ model ->
        onNavigateTo(model)
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    BaseContentWrapper(
        state = state
    ) {
        WishlistAddScreen(
            state = state, onAction = viewModel::onAction, contentPadding = contentPadding
        )

    }
}

@Composable
fun WishlistAddScreen(
    state: WishlistAddState,
    onAction: (action: WishlistAddAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {

    Column(Modifier.fillMaxSize().padding(contentPadding).padding(horizontal = 16.dp)) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())

                .weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            AppTopBar(
                title = UiText.StringResourceId(Res.string.add_new).asString(),
                onNavIconClick = { onAction(WishlistAddAction.OnBackClick) },
                isBack = true
            )

            // 1. Görsel seçimi
            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(12.dp))
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
                label = {
                    Text(
                        UiText.StringResourceId(Res.string.wishlist_label_product_name).asString()
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            // 3. Fiyat

            MoneyInput(
                money = state.wishlistItem.price,
                onValueChange = { onAction(WishlistAddAction.OnPriceChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = UiText.StringResourceId(
                    Res.string.wishlist_label_price,
                    arrayOf(state.wishlistItem.price.currency.symbol)
                ),
                //isError = state.isPriceError,
                //errorMessage = state.priceErrorMessage
            )

            // 4. Kategori
            CategorySelector(
                categories = state.wishlistCategories,
                selectedCategory = state.wishlistItem.category
                    ?: state.wishlistCategories.firstOrNull(),
                onCategorySelected = {
                    onAction(WishlistAddAction.OnCategorySelected(it))
                },
                onNewCategoryCreated = {
                    onAction(WishlistAddAction.OnNewCategoryCreated(it))
                },
                categoryType = CategoryType.WISHLIST,
            )

            // 5. Öncelik (1-5)
            PriorityChooser(
                value = state.wishlistItem.priority,
                onValueChange = { onAction(WishlistAddAction.OnPriorityChanged(it)) })

            // 6. Not
            OutlinedTextField(
                value = state.wishlistItem.note ?: "",
                onValueChange = { onAction(WishlistAddAction.OnNoteChanged(it)) },
                label = {
                    Text(
                        UiText.StringResourceId(Res.string.wishlist_label_note_optional).asString()
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            // 7. Satın alındı mı?
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.wishlistItem.isPurchased,
                    onCheckedChange = { onAction(WishlistAddAction.OnPurchasedChanged(it)) })
                Spacer(modifier = Modifier.width(8.dp))
                Text(UiText.StringResourceId(Res.string.wishlist_checkbox_purchased).asString())
            }

        }
        AppButton(
            text = UiText.StringResourceId(Res.string.wishlist_button_save).asString(),
            onClick = { onAction(WishlistAddAction.OnSaveClicked) },
            loading = state.isLoading,
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
        )
    }

}