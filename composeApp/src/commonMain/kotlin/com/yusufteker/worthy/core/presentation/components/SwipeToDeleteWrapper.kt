package com.yusufteker.worthy.core.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun SwipeToDeleteWrapper(
    modifier: Modifier = Modifier,

    shape: Shape = CardDefaults.shape,
    onDelete: () -> Unit,
    content: @Composable () -> Unit,
) {
    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.StartToEnd, SwipeToDismissBoxValue.EndToStart -> {
                    onDelete()
                    true
                }

                SwipeToDismissBoxValue.Settled -> false
            }
        })

    // Eğer sürüklenme olduysa (ama henüz bırakılmadıysa), ikonu döndür
    val isSwiping =
        swipeState.currentValue == SwipeToDismissBoxValue.Settled && swipeState.targetValue != SwipeToDismissBoxValue.Settled

    val rotation by animateFloatAsState(
        targetValue = if (isSwiping) -45f else 0f, label = "DeleteIconRotation"
    )


    SwipeToDismissBox(
        state = swipeState, backgroundContent = {
            Box(
                modifier = Modifier.clip(shape).fillMaxSize()
                    .background(Color.Red.copy(alpha = 0.8f)),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier.padding(16.dp).rotate(rotation)
                )
            }
        }, modifier = modifier
    ) {
        content()
    }
}