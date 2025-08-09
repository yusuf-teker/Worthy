package com.yusufteker.worthy.core.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.presentation.theme.AppColors
import org.jetbrains.compose.resources.painterResource
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.bottom_icon_graph
import worthy.composeapp.generated.resources.bottom_icon_wallet
import worthy.composeapp.generated.resources.bottom_icon_wishlist


@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onItemSelected: (Routes) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem(
            "Home",
            painter = rememberVectorPainter(Icons.Filled.Home),
            route = Routes.Dashboard
        ),
        BottomNavItem(
            "Details",
            painter = painterResource(Res.drawable.bottom_icon_graph),
            route = Routes.Trends
        ),
        BottomNavItem(
            "AddTransaction",
            painter = rememberVectorPainter(Icons.Filled.Add),
            route = Routes.AddTransaction
        ),
        /*BottomNavItem(
            "Wallet",
            painter = painterResource(Res.drawable.bottom_icon_wallet),
            route = Routes.Wallet
        ),*/
        BottomNavItem(
            "WishlistGraph",
            painter = painterResource(Res.drawable.bottom_icon_wishlist),
            route = Routes.WishlistGraph
        ),
        BottomNavItem(
            "Settings",
            painter = rememberVectorPainter(Icons.Filled.Settings),
            route = Routes.Settings
        )
    )

    Surface(
        modifier = modifier,
        color = AppColors.transparent,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    NavItem(
                        item = item, // todo current route ile item.route karşılaştırılacak
                        selected = item.route.toString().contains( currentRoute , true),
                        onClick = {
                            onItemSelected(item.route)
                        }
                    )
                }
            }
        }
    }
}

data class BottomNavItem(
    val label: String,
    val painter: Painter,
    val route: Routes
)

@Composable
fun NavItem(
    item: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) AppColors.primary.copy(alpha = 0.4f) else Color.Transparent,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "backgroundColorAnimation"
    )

    val iconColor by animateColorAsState(
        targetValue = if (selected) AppColors.primary else AppColors.onSurface.copy(alpha = 0.6f),
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "iconColorAnimation"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp)
            .height(36.dp)
    ) {
        Icon(
            painter = item.painter,
            contentDescription = item.label,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
    }
}
