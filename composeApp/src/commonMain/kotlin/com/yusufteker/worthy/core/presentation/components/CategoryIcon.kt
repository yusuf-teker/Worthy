package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.bottom_icon_graph
import worthy.composeapp.generated.resources.bottom_icon_wallet
import worthy.composeapp.generated.resources.bottom_icon_wishlist

@Composable
fun CategoryIcon(iconName: String?) {
    iconName?.let {
        if (it.startsWith("category_icon")){
            val iconResId = when (iconName) {
                "ic_food" -> Res.drawable.bottom_icon_graph
                "ic_shopping" -> Res.drawable.bottom_icon_wishlist
                else -> Res.drawable.bottom_icon_wallet
            }

            Icon(
                painter = painterResource(iconResId),
                contentDescription = null
            )
        }
        else {
            Text(
                text = it,
                modifier = Modifier.padding(end = 4.dp)
            )
        }
    }

}


