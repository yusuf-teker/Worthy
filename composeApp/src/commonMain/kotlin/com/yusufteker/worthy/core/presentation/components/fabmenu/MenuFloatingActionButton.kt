package com.yusufteker.worthy.core.presentation.components.fabmenu

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.Constants.fabIconSize

/**
 * Dışarıdan ImageVector ikon alarak kullanılabilen versiyon.
 */
@Composable
fun MenuFloatingActionButton(
    srcIcon: ImageVector,
    items: SnapshotStateList<MenuFabItem>,
    modifier: Modifier = Modifier,
    menuFabState: MenuFabState = rememberMenuFabState(),
    srcIconColor: Color = Color.White,
    fabBackgroundColor: Color = Color.Unspecified,
    showLabels: Boolean = true,
    onFabItemClicked: (item: MenuFabItem) -> Unit
) {
    MenuFloatingActionButton(
        icon = {
            Icon(
                imageVector = srcIcon,
                modifier = Modifier.rotate(it.value),
                tint = srcIconColor,
                contentDescription = null
            )
        },
        items = items,
        modifier = modifier,
        menuFabState = menuFabState,
        fabBackgroundColor = fabBackgroundColor,
        showLabels = showLabels,
        onFabItemClicked = onFabItemClicked

    )
}

/**
 * Ana Menu FAB composable.
 *
 * @param icon FAB'ın ana ikonu (animasyonlu dönüş için State<Float> parametresi alır)
 * @param items Menüde yer alan öğeler listesi
 * @param menuFabState FAB'ın açık/kapalı durumunu tutan state
 * @param fabBackgroundColor Ana FAB'ın arka plan rengi
 * @param showLabels Menü öğelerinin yanındaki etiketlerin gösterilip gösterilmeyeceği
 * @param onFabItemClicked Menü öğesine tıklanınca çalışacak callback
 */
@Composable
fun MenuFloatingActionButton(
    icon: @Composable (rotateAnim: State<Float>) -> Unit,
    items: SnapshotStateList<MenuFabItem>,
    modifier: Modifier = Modifier,
    menuFabState: MenuFabState = rememberMenuFabState(),
    fabBackgroundColor: Color = Color.Unspecified,
    showLabels: Boolean = true,
    onFabItemClicked: (item: MenuFabItem) -> Unit,
) {
    val transition = updateTransition(
        targetState = menuFabState.menuFabStateEnum.value, label = "menuFabStateEnum"
    )
    val rotateAnim = transition.animateFloat(
        transitionSpec = {
            if (targetState == MenuFabStateEnum.Expanded) {
                spring(stiffness = Spring.StiffnessLow)
            } else {
                spring(stiffness = Spring.StiffnessMedium)
            }
        }, label = "rotateAnim"
    ) { state ->
        if (state == MenuFabStateEnum.Collapsed) 0F else -45F
    }
    val alphaAnim: Float by transition.animateFloat(transitionSpec = {
        tween(durationMillis = 200)
    }, label = "alphaAnim") { state ->
        if (state == MenuFabStateEnum.Expanded) 1F else 0F
    }
    Box(modifier = modifier, contentAlignment = Alignment.BottomEnd) {
        items.forEachIndexed { index, item ->
            // Öğelerin yukarı doğru açılma mesafesi animasyonu

            val shrinkAnim by transition.animateFloat(targetValueByState = { state ->
                when (state) {
                    MenuFabStateEnum.Collapsed -> 5F
                    MenuFabStateEnum.Expanded -> (index + 1) * 60F + if (index == 0) 5F else 0F
                }
            }, label = "shrinkAnim", transitionSpec = {
                if (targetState == MenuFabStateEnum.Expanded) {
                    spring(stiffness = Spring.StiffnessLow, dampingRatio = 0.58F)
                } else {
                    spring(stiffness = Spring.StiffnessMedium)
                }
            })
            // Her bir menü öğesi (yatay sıra: [Etiket] [FAB Butonu])

            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(
                        bottom = shrinkAnim.dp, top = 5.dp, end = 30.dp
                    ).alpha(alphaAnim)
            ) {
                if (showLabels) {
                    Text(
                        item.label,
                        color = item.labelTextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.alpha(alphaAnim)
                            .background(color = item.labelBackgroundColor)
                            .padding(start = 6.dp, end = 6.dp, top = 4.dp, bottom = 4.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                }
                FloatingActionButton(
                    containerColor = if (item.fabBackgroundColor == Color.Unspecified) AppColors.primary else item.fabBackgroundColor,
                    modifier = Modifier.size(fabIconSize),
                    onClick = {
                        menuFabState.menuFabStateEnum.value = MenuFabStateEnum.Collapsed
                        onFabItemClicked(item)
                    },
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 2.dp, pressedElevation = 4.dp
                    )
                ) {
                    item.icon()
                }
            }
        }
        // Ana FAB butonu
        FloatingActionButton(
            modifier = Modifier.padding(0.dp, end = 25.dp),
            containerColor = if (fabBackgroundColor == Color.Unspecified) AppColors.primary else fabBackgroundColor,
            onClick = {
                menuFabState.menuFabStateEnum.value =
                    if (menuFabState.menuFabStateEnum.value == MenuFabStateEnum.Collapsed) MenuFabStateEnum.Expanded else MenuFabStateEnum.Collapsed
            }) {
            icon(rotateAnim)
        }
    }
}

@Composable
fun MenuFloatingActionButtonWithScale(
    srcIcon: ImageVector,
    items: SnapshotStateList<MenuFabItem>,
    modifier: Modifier = Modifier,
    menuFabState: MenuFabState = rememberMenuFabState(),
    srcIconColor: Color = Color.White,
    fabBackgroundColor: Color = Color.Unspecified,
    showLabels: Boolean = true,
    onFabItemClicked: (item: MenuFabItem) -> Unit
) {
    val transition = updateTransition(
        targetState = menuFabState.menuFabStateEnum.value, label = "menuFabStateEnum"
    )

    // Ana FAB ikonu dönüş animasyonu
    val rotateAnim = transition.animateFloat(
        transitionSpec = {
            spring(stiffness = Spring.StiffnessLow)
        }, label = "rotateAnim"
    ) { state ->
        if (state == MenuFabStateEnum.Collapsed) 0F else -45F
    }

    Box(modifier = modifier, contentAlignment = Alignment.BottomEnd) {
        items.forEachIndexed { index, item ->
            // Fade animasyonu
            val alphaAnim by transition.animateFloat(
                label = "alphaAnim",
                transitionSpec = {
                    tween(
                        durationMillis = 200,
                        delayMillis = index * 50
                    )
                }) { state ->
                if (state == MenuFabStateEnum.Expanded) 1f else 0f
            }

            // Scale animasyonu
            val scaleAnim by transition.animateFloat(
                label = "scaleAnim",
                transitionSpec = { spring(stiffness = Spring.StiffnessLow) }) { state ->
                if (state == MenuFabStateEnum.Expanded) 1f else 0.8f
            }

            // Yükseklik animasyonu (yukarı doğru açılma mesafesi)
            val offsetAnim by transition.animateDp(
                label = "offsetAnim",
                transitionSpec = { spring(stiffness = Spring.StiffnessLow) }) { state ->
                if (state == MenuFabStateEnum.Expanded) ((index + 1) * 60).dp else 0.dp
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = offsetAnim, end = 30.dp).alpha(alphaAnim)
                    .scale(scaleAnim)
            ) {
                if (showLabels) {
                    Text(
                        text = item.label,
                        color = item.labelTextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.alpha(alphaAnim).background(
                                color = item.labelBackgroundColor,
                                shape = RoundedCornerShape(8.dp) // köşeleri yuvarla
                            ).padding(horizontal = 8.dp, vertical = 4.dp) // iç boşluk
                    )

                    Spacer(Modifier.width(16.dp))
                }
                FloatingActionButton(
                    containerColor = if (item.fabBackgroundColor == Color.Unspecified) AppColors.primary else item.fabBackgroundColor,
                    modifier = Modifier.size(fabIconSize),
                    onClick = {
                        menuFabState.menuFabStateEnum.value = MenuFabStateEnum.Collapsed
                        onFabItemClicked(item)
                    },
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 2.dp, pressedElevation = 4.dp
                    )
                ) {
                    item.icon()
                }
            }
        }

        // Ana FAB butonu
        FloatingActionButton(
            modifier = Modifier.padding(end = 25.dp),
            containerColor = if (fabBackgroundColor == Color.Unspecified) AppColors.primary else fabBackgroundColor,
            onClick = {
                menuFabState.menuFabStateEnum.value =
                    if (menuFabState.menuFabStateEnum.value == MenuFabStateEnum.Collapsed) MenuFabStateEnum.Expanded else MenuFabStateEnum.Collapsed
            }) {
            Icon(
                imageVector = srcIcon,
                modifier = Modifier.rotate(rotateAnim.value),
                tint = srcIconColor,
                contentDescription = null
            )
        }
    }
}

@Composable
fun MenuFloatingActionButtonWithScale(
    icon: @Composable (rotateAnim: State<Float>) -> Unit,
    items: SnapshotStateList<MenuFabItem>,
    modifier: Modifier = Modifier,
    menuFabState: MenuFabState = rememberMenuFabState(),
    fabBackgroundColor: Color = Color.Unspecified,
    showLabels: Boolean = true,
    onFabItemClicked: (item: MenuFabItem) -> Unit,
) {
    val transition = updateTransition(
        targetState = menuFabState.menuFabStateEnum.value, label = "menuFabStateEnum"
    )

    // Ana FAB dönüş animasyonu
    val rotateAnim = transition.animateFloat(
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) }, label = "rotateAnim"
    ) { state ->
        if (state == MenuFabStateEnum.Collapsed) 0f else -45f
    }

    Box(modifier = modifier, contentAlignment = Alignment.BottomEnd) {
        items.forEachIndexed { index, item ->
            val alphaAnim by transition.animateFloat(
                label = "alphaAnim",
                transitionSpec = {
                    tween(
                        durationMillis = 200,
                        delayMillis = index * 50
                    )
                }) { state ->
                if (state == MenuFabStateEnum.Expanded) 1f else 0f
            }

            val scaleAnim by transition.animateFloat(
                label = "scaleAnim",
                transitionSpec = { spring(stiffness = Spring.StiffnessLow) }) { state ->
                if (state == MenuFabStateEnum.Expanded) 1f else 0.8f
            }

            val offsetAnim by transition.animateDp(
                label = "offsetAnim",
                transitionSpec = { spring(stiffness = Spring.StiffnessLow) }) { state ->
                if (state == MenuFabStateEnum.Expanded) ((index + 1) * 60).dp else 0.dp
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = offsetAnim, end = 30.dp).alpha(alphaAnim)
                    .scale(scaleAnim)
            ) {
                if (showLabels) {
                    Text(
                        text = item.label,
                        color = item.labelTextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.background(item.labelBackgroundColor)
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                }
                FloatingActionButton(
                    containerColor = if (item.fabBackgroundColor == Color.Unspecified) AppColors.primary else item.fabBackgroundColor,
                    modifier = Modifier.size(fabIconSize),
                    onClick = {
                        menuFabState.menuFabStateEnum.value = MenuFabStateEnum.Collapsed
                        onFabItemClicked(item)
                    },
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 2.dp, pressedElevation = 4.dp
                    )
                ) {
                    item.icon()
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier.padding(end = 25.dp),
            containerColor = if (fabBackgroundColor == Color.Unspecified) AppColors.primary else fabBackgroundColor,
            onClick = {
                menuFabState.menuFabStateEnum.value =
                    if (menuFabState.menuFabStateEnum.value == MenuFabStateEnum.Collapsed) MenuFabStateEnum.Expanded else MenuFabStateEnum.Collapsed
            }) {
            icon(rotateAnim)
        }
    }
}

@Composable
fun AddFabMenu(
    items: List<MenuFabItem>,
    visible: Boolean, // dışarıdan kontrol
    modifier: Modifier = Modifier,
    showLabels: Boolean = true,
    onFabItemClicked: (item: MenuFabItem) -> Unit
) {
    val targetState = if (visible) 1 else 0
    val transition = updateTransition(targetState, label = "fabMenuVisibility")

    Box(
        modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd
    ) {
        Row(Modifier.fillMaxWidth()) {
            Box(
                Modifier.weight(1f).offset(x = 24.dp), contentAlignment = Alignment.BottomEnd
            ) {
                items.forEachIndexed { index, item ->
                    val alpha by transition.animateFloat(
                        transitionSpec = { tween(durationMillis = 250, delayMillis = index * 50) },
                        label = "alphaAnim$index"
                    ) { state -> if (state == 1) 1f else 0f }

                    val scale by transition.animateFloat(
                        transitionSpec = { spring(stiffness = Spring.StiffnessLow) },
                        label = "scaleAnim$index"
                    ) { state -> if (state == 1) 1f else 0.8f }

                    val offset by transition.animateDp(
                        transitionSpec = { spring(stiffness = Spring.StiffnessLow) },
                        label = "offsetAnim$index"
                    ) { state -> if (state == 1) ((index) * 60).dp else 0.dp }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = offset).alpha(alpha).scale(scale)
                    ) {
                        if (showLabels) {
                            Text(
                                text = item.label,
                                color = item.labelTextColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.background(
                                        color = item.labelBackgroundColor,
                                        shape = RoundedCornerShape(8.dp)
                                    ).padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                            Spacer(Modifier.width(16.dp))
                        }

                        FloatingActionButton(
                            onClick = { onFabItemClicked(item) },
                            modifier = Modifier.size(fabIconSize),
                            containerColor = if (item.fabBackgroundColor == Color.Unspecified) AppColors.primary else item.fabBackgroundColor,
                            elevation = FloatingActionButtonDefaults.elevation(
                                defaultElevation = 2.dp, pressedElevation = 4.dp
                            )
                        ) {
                            item.icon()
                        }
                    }
                }

            }
            Box(Modifier.weight(1f)) {

            }
        }
    }
}
