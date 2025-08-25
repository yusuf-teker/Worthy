package com.yusufteker.worthy.screen.subscription.add.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.theme.AppColors
import com.yusufteker.worthy.screen.subscription.domain.model.SubscriptionCategory
import com.yusufteker.worthy.screen.subscription.domain.model.getDisplayName
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.cancel
import worthy.composeapp.generated.resources.category_label
import worthy.composeapp.generated.resources.create_new_category_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionCategorySelector(
    categories: List<SubscriptionCategory>,
    selectedCategory: SubscriptionCategory?,
    modifier: Modifier = Modifier,
    onCategorySelected: (SubscriptionCategory?) -> Unit,
    onNewCategoryCreated: (SubscriptionCategory) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedCategory?.getDisplayName() ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(UiText.StringResourceId(Res.string.category_label).asString()) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            leadingIcon = if (selectedCategory != null) {
                { Text(selectedCategory.defaultIcon) }
            } else null,
            modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.getDisplayName()) },
                    leadingIcon = { Text(category.defaultIcon) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }

            DropdownMenuItem(
                text = {
                    Text(
                        UiText.StringResourceId(Res.string.create_new_category_title).asString(),
                        fontWeight = FontWeight.SemiBold,
                        color = AppColors.primary
                    )
                },
                onClick = {
                    expanded = false
                    showDialog = true
                }
            )
        }
    }

    var newCategoryName by remember { mutableStateOf("") }
    var selectedEmoji by remember { mutableStateOf("📦") } // default emoji
    val emojiOptionsList = listOf("🎬", "🎵", "💻", "🎮", "📺", "🎧", "📚", "🍿")

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(UiText.StringResourceId(Res.string.create_new_category_title).asString()) },
            text = {
                Column {
                    OutlinedTextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        label = { Text("Category Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))
                    FlowRow(
                        modifier = Modifier.height(256.dp).verticalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        emojiOptionsList.forEach { emoji ->
                            Text(
                                text = emoji,
                                fontSize = 24.sp,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(
                                        if (emoji == selectedEmoji) Color.Gray.copy(alpha = 0.2f)
                                        else Color.Transparent
                                    )
                                    .clickable { selectedEmoji = emoji }
                                    .padding(all = 8.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newCategoryName.isNotBlank()) {
                        //TODO DUMMY EKRAN KATEGORİ EKLEME GELECEK
                        val newCategory = SubscriptionCategory.OTHER
                        onNewCategoryCreated(newCategory)
                        onCategorySelected(newCategory)
                        showDialog = false
                        newCategoryName = ""
                    }
                }) {
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    newCategoryName = ""
                }) {
                    Text(UiText.StringResourceId(Res.string.cancel).asString())
                }
            }
        )
    }
}
