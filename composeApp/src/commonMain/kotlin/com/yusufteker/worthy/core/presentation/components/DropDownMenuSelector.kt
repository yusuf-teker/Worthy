package com.yusufteker.worthy.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun DropdownMenuSelector(
    options: List<Int>,
    selected: Int?,
    isNullable: Boolean = false,
    onSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(text = selected?.toString() ?: "Yok")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            if (isNullable) {
                DropdownMenuItem(
                    text = {
                        Text("Yok")
                    },
                    onClick = {
                        onSelected(-1)
                        expanded = false
                    })
            }
            options.forEach {
                DropdownMenuItem(
                    text = {
                        Text(it.toString())
                    },
                    onClick = {
                        onSelected(it)
                        expanded = false
                    }
                )
            }
        }
    }
}