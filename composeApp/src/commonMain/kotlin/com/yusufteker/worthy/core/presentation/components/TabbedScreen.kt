package com.yusufteker.worthy.core.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.core.presentation.theme.AppColors
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TabbedScreen(
    initialPage: Int = 0,
    onTabChanged: (Int) -> Unit,
    selectedTabColor: Color = AppColors.primary.copy(alpha = 0.4f),
    unselectedTabColor: Color = AppColors.transparent,
    screens: List<Screen>
) {

    LaunchedEffect(true) {
        onTabChanged.invoke(initialPage)
    }
    Column(
        modifier = Modifier.fillMaxSize().background(Color.Transparent)
    ) {
        val tabs = screens.map {
            TabRowItem(it.title)
        }
        val pagerState = rememberPagerState(
            initialPage = initialPage, initialPageOffsetFraction = 0f
        ) {
            screens.size
        }

        val coroutineScope = rememberCoroutineScope()

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            tabs = tabs,
            selectedTabColor = selectedTabColor,
            unselectedTabColor = unselectedTabColor,
            onTabChanged = { index, _ ->
                onTabChanged(index)
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            })


        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize().weight(1f),

            ) { page ->
            screens[page].content()
        }
    }
}

@Preview
@Composable
fun TabbedScreenPreview() {

    val screenExample = Screen(
        title = "Example Tab Title",
        content = {
            Box(
                modifier = Modifier.fillMaxSize().background(AppColors.background),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "EXAMPLE SCREEN CONTENT")
            }

        },
    )
    TabbedScreen(
        initialPage = 0,
        onTabChanged = {},
        screens = listOf(screenExample, screenExample),
        selectedTabColor = AppColors.background,
        unselectedTabColor = AppColors.background,
    )
}

data class Screen(
    val title: String,
    val content: @Composable () -> Unit,
)

@Composable
fun TabRow(
    selectedTabIndex: Int = 0,
    tabs: List<TabRowItem>,
    selectedTabColor: Color = AppColors.primary.copy(alpha = 0.4f),
    unselectedTabColor: Color = AppColors.transparent,
    onTabChanged: (Int, TabRowItem) -> Unit,
) {

    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = Color.Transparent,
        indicator = {},
        divider = {},
        modifier = Modifier.padding(4.dp).clip(RoundedCornerShape(12.dp)).border(
                width = 1.dp,
                color = AppColors.primary.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp)
            )

    ) {
        tabs.forEachIndexed { index, screen ->
            val selected = selectedTabIndex == index
            // Animasyonlu renk
            val backgroundColor by animateColorAsState(
                targetValue = if (selected) selectedTabColor else unselectedTabColor,
                animationSpec = tween(durationMillis = 300), // geçiş süresi
                label = "TabBackgroundColor"
            )
            Box(
                modifier = Modifier.fillMaxSize().background(backgroundColor),

                ) {
                Tab(
                    modifier = Modifier.background(Color.Transparent).fillMaxWidth()
                        .align(Alignment.Center),
                    text = {
                        Text(
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = screen.title,
                            style = TextStyle.Default.copy(
                                color = if (selected) AppColors.primary else AppColors.onSurface.copy(
                                    alpha = 0.6f
                                ),
                            )
                        )
                    },
                    selected = selectedTabIndex == index,
                    onClick = {
                        onTabChanged.invoke(index, tabs[index])
                    },

                    )
            }

        }
    }
}

data class TabRowItem(
    val title: String
)

@Preview
@Composable
fun TabRowPreview() {
    val tabIndex = remember {
        mutableIntStateOf(0)
    }

    TabRow(
        tabIndex.intValue, tabs = listOf(
            TabRowItem("TAB TITLE 1"), TabRowItem("TAB TITLE 2")
        ), onTabChanged = { index, _ ->
            tabIndex.intValue = index
        })

}