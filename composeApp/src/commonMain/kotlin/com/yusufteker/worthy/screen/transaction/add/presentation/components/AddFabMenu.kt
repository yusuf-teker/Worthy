package com.yusufteker.worthy.screen.transaction.add.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.components.fabmenu.AddFabMenu
import com.yusufteker.worthy.core.presentation.components.fabmenu.MenuFabItem
import com.yusufteker.worthy.core.presentation.theme.AppColors
import org.jetbrains.compose.resources.painterResource
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.add
import worthy.composeapp.generated.resources.card
import worthy.composeapp.generated.resources.expense
import worthy.composeapp.generated.resources.expenses
import worthy.composeapp.generated.resources.income

@Composable
fun AddFabMenu(
    showMenu: Boolean,
    modifier: Modifier,
    onMenuClick: (MenuFabItem) -> Unit
) {
    val menuItems = rememberAddFabItems()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd
    ) {
        AddFabMenu(
            items = menuItems,
            visible = showMenu,
            onFabItemClicked = { item ->
                onMenuClick(item)
            }
        )
    }
}

@Composable
fun rememberAddFabItems(): SnapshotStateList<MenuFabItem> {
    val fabColor = AppColors.primary
    val expenseFabColor = AppColors.icon_red
    val incomeFabColor = AppColors.icon_green


    val expenseLabel = UiText.StringResourceId(Res.string.expenses).asString()
    val incomeLabel = UiText.StringResourceId(Res.string.income).asString()
    val cardLabel = UiText.StringResourceId(Res.string.card).asString()

    return remember {
        mutableStateListOf(
            MenuFabItem(
                label = expenseLabel,
                icon = {
                    Icon(
                        painter = painterResource(Res.drawable.expense),
                        contentDescription = null,
                        tint = Color.White
                    )
                },
                labelBackgroundColor = Color.Black.copy(alpha = 0.6f),
                labelTextColor = Color.White,
                fabBackgroundColor = expenseFabColor
            ),
            MenuFabItem(
                label = incomeLabel,
                icon = {
                    Icon(
                        painter = painterResource(Res.drawable.income),
                        contentDescription = null,
                        tint = Color.White
                    )
                },
                labelBackgroundColor = Color.Black.copy(alpha = 0.6f),
                labelTextColor = Color.White,
                fabBackgroundColor = incomeFabColor
            ),
            MenuFabItem(
                label = cardLabel,
                icon = {
                    Icon(
                        painter = painterResource(Res.drawable.card),
                        contentDescription = null,
                        tint = Color.White
                    )
                },
                labelBackgroundColor = Color.Black.copy(alpha = 0.6f),
                labelTextColor = Color.White,
                fabBackgroundColor = fabColor
            )
        )
    }
}
