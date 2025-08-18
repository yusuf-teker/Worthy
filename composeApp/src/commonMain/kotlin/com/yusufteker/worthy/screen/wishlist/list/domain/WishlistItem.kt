package com.yusufteker.worthy.screen.wishlist.list.domain

import androidx.compose.ui.graphics.Color
import com.yusufteker.worthy.core.domain.model.Category
import com.yusufteker.worthy.core.domain.model.Money
import com.yusufteker.worthy.core.domain.model.emptyMoney
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.theme.AppColors.priorityColors
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.suggestion_book
import worthy.composeapp.generated.resources.suggestion_game_console
import worthy.composeapp.generated.resources.suggestion_headphones
import worthy.composeapp.generated.resources.suggestion_phone
import worthy.composeapp.generated.resources.suggestion_vacation

data class WishlistItem(
    val id: Int = 0,
    val name: String = "",
    val price: Money = emptyMoney(),
    val category: Category? = null,
    val priority: Int = 0,
    val isPurchased: Boolean = false,
    val addedDate: Long = -1,
    val purchasedDate: Long? = null,
    val note: String? = "",
    val imageUri: String? = ""
)

val WishlistItem.priorityColor: Color
    get() {
        return priorityColors.getOrElse(priority) { Color.Gray }
    }

val generalSuggestions: List<UiText>
    get() = listOf(
        UiText.StringResourceId(Res.string.suggestion_phone),
        UiText.StringResourceId(Res.string.suggestion_headphones),
        UiText.StringResourceId(Res.string.suggestion_vacation),
        UiText.StringResourceId(Res.string.suggestion_game_console),
        UiText.StringResourceId(Res.string.suggestion_book)
    )
