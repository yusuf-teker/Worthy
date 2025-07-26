package com.yusufteker.worthy.core.presentation.components



import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun SearchBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    color: Color = MaterialTheme.colorScheme.background
) {

    var isTextFieldFocused by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val iconSize = 40.dp
    val iconSpacing = 10.dp

    val scrollOffset by animateDpAsState(
        targetValue = when (isTextFieldFocused) {
            true -> 0.dp // 2 icon
            false -> (iconSize + iconSpacing) // 1 icon
        },
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        )
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = color
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier.weight(1f)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isTextFieldFocused = focusState.isFocused
                },
            value = text,
            onValueChange = {
                onTextChange.invoke(it)
                if (it.isNotEmpty()) isTextFieldFocused = true
            },
            label = { Text("Search") })


        Row( modifier = Modifier.offset(x = scrollOffset)) {
            IconButton(onClick = {
                onSearch.invoke()
            },
                modifier = Modifier.animateContentSize()) {
                Icon(
                    imageVector = Icons.Default.Search, contentDescription = "Search"
                )
            }
            IconButton(modifier = Modifier, onClick = {
                onClear.invoke()
                focusManager.clearFocus()
            }) {
                Icon(
                    imageVector = Icons.Default.Clear, contentDescription = "Clear"
                )
            }
        }
    }
}

@Preview
@Composable
fun SearchBarPreview() {
    SearchBar(
        text = "",
        onTextChange = {},
        onSearch = {},
        onClear = {},
    )
}
