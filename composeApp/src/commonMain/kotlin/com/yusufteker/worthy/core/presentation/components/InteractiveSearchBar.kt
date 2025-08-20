package com.yusufteker.worthy.core.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.core.presentation.theme.AppTypography
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.clear_search
import worthy.composeapp.generated.resources.close
import worthy.composeapp.generated.resources.history
import worthy.composeapp.generated.resources.no_results
import worthy.composeapp.generated.resources.search_hint
import worthy.composeapp.generated.resources.search_history_label

data class SearchResult(
    val id: Int, val title: String, val description: String
)

@Composable
fun InteractiveSearchBar(
    modifier: Modifier = Modifier,
    query: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    searchResult: List<SearchResult> = emptyList(),
    searchHistory: List<String> = emptyList(),
    searchSuggestions: List<String> = emptyList(),
    onHistoryItemClick: (String) -> Unit = {},
    placeholder: String,
    isFocused: Boolean = false
) {
    var isFocused by remember { mutableStateOf(isFocused) }
    var isSearching by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // Animasyonlar
    val cornerRadius by animateDpAsState(
        targetValue = if (!isFocused) 25.dp else 0.dp,
        animationSpec = tween(300),
        label = "cornerRadius"
    )

    val horizontalPadding by animateDpAsState(
        targetValue = if (!isFocused) 24.dp else 0.dp,
        animationSpec = tween(300),
        label = "horizontalPadding"
    )

    // Arama fonksiyonu
    LaunchedEffect(query) {
        if (query.isNotEmpty()) {
            isSearching = true
            delay(300) // Debounce
            isSearching = false
        } else {
        }
    }

    Column(
        modifier = modifier.fillMaxWidth().animateContentSize()
    ) {
        // Ana arama barı
        Box(
            modifier = Modifier.fillMaxWidth().padding(horizontal = horizontalPadding)
                .clip(RoundedCornerShape(cornerRadius)).background(
                    if (!isFocused) AppColors.surface
                    else Color.Transparent
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(
                    // horizontal = if (!isFocused) 16.dp else 0.dp,
                    vertical = 12.dp
                ), verticalAlignment = Alignment.CenterVertically
            ) {
                // Arama ikonu
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = UiText.StringResourceId(Res.string.search_hint).asString(),
                    tint = if (!isFocused) AppColors.primary
                    else AppColors.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Text field
                BasicTextField(
                    value = query,
                    onValueChange = { onSearchQueryChange(it) },
                    modifier = Modifier.weight(1f).focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                        },
                    textStyle = TextStyle(
                        fontSize = 16.sp, color = AppColors.onSurface
                    ),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (query.isEmpty()) {
                            Text(
                                text = placeholder, style = TextStyle(
                                    fontSize = 16.sp, color = AppColors.onSurfaceVariant
                                )
                            )
                        }
                        innerTextField()
                    })

                if (isFocused) {
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (query.isNotEmpty()) {
                                onSearchQueryChange("")
                            }
                            focusManager.clearFocus()

                        }, modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = if (query.isNotEmpty()) UiText.StringResourceId(Res.string.clear_search)
                                .asString() else UiText.StringResourceId(Res.string.close)
                                .asString(),
                            tint = AppColors.onSurfaceVariant
                        )
                    }
                }

            }
        }

        // Alt çizgi (focus değilken)
        if (isFocused) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                thickness = 1.dp,
                color = AppColors.outline
            )
        }

        // Sonuçlar veya geçmiş
        if (isFocused) {
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
                    //.heightIn(max = 300.dp)
                    .fillMaxHeight().pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                            isFocused = false
                        })
                    }) {
                if (query.isEmpty() && searchHistory.isNotEmpty()) {
                    // Geçmiş aramalar //TODO kaldırılabilir
                    item {
                        Text(
                            text = UiText.StringResourceId(Res.string.search_history_label)
                                .asString(),
                            style = AppTypography.labelMedium,
                            color = AppColors.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 0.dp, vertical = 8.dp)
                        )
                    }

                    if (searchHistory.size > 4) {
                        items(searchHistory.take(5)) { historyItem ->
                            SearchSuggestion(
                                text = historyItem, onClick = {
                                    onSearchQueryChange(historyItem)
                                    onHistoryItemClick(historyItem)
                                }, isHistory = true

                            )
                        }
                    } else {
                        items(searchHistory) { historyItem ->
                            SearchSuggestion(
                                text = historyItem, onClick = {
                                    onSearchQueryChange(historyItem)
                                    //onHistoryItemClick(historyItem)
                                }, isHistory = true
                            )
                        }
                        items(searchSuggestions) { suggestion ->
                            SearchSuggestion(
                                text = suggestion, onClick = {
                                    onSearchQueryChange(suggestion)
                                })
                        }
                    }

                } else if (query.isNotEmpty()) {
                    // Arama sonuçları
                    if (isSearching) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp), strokeWidth = 2.dp
                                )
                            }
                        }
                    } else {
                        if (searchResult.isEmpty()) {
                            item {
                                Text(
                                    text = UiText.StringResourceId(Res.string.no_results)
                                        .asString(),
                                    style = AppTypography.bodyMedium,
                                    color = AppColors.onSurfaceVariant,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        } else {
                            items(searchResult) { result ->
                                SearchResultItem(
                                    result = result, onClick = {
                                        onSearchQueryChange(result.title)
                                        focusManager.clearFocus()
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchSuggestion(
    text: String, onClick: () -> Unit, isHistory: Boolean = false
) {
    Row(modifier = Modifier.fillMaxWidth().clickable { onClick() }
        .padding(horizontal = 0.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically) {
        if (isHistory) {
            Icon(
                painter = painterResource(Res.drawable.history),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = AppColors.onSurfaceVariant
            )
        } else {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = AppColors.onSurfaceVariant
            )
        }


        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text, style = AppTypography.bodyMedium, color = AppColors.onSurface
        )
    }
}

@Composable
private fun SearchResultItem(
    result: SearchResult, onClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().clickable { onClick() }
        .padding(horizontal = 16.dp, vertical = 12.dp)) {
        Text(
            text = result.title,
            style = AppTypography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = AppColors.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = result.description,
            style = AppTypography.bodySmall,
            color = AppColors.onSurfaceVariant
        )
    }
}
