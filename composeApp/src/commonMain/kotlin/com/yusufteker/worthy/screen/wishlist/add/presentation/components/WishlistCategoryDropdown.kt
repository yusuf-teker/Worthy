package com.yusufteker.worthy.screen.wishlist.add.presentation.components

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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yusufteker.worthy.core.domain.model.Category

import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusufteker.worthy.core.domain.model.CategoryType
import com.yusufteker.worthy.core.domain.model.emojiOptions
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.components.CategoryIcon
import com.yusufteker.worthy.core.presentation.theme.AppColors
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.add_new_category
import worthy.composeapp.generated.resources.cancel
import worthy.composeapp.generated.resources.category_label
import worthy.composeapp.generated.resources.category_name_label
import worthy.composeapp.generated.resources.create_button
import worthy.composeapp.generated.resources.create_new_category_title


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistCategoryDropdown(
    categories: List<Category>,
    selectedCategory: Category?,
    modifier: Modifier = Modifier,
    onCategorySelected: (Category) -> Unit,
    onNewCategoryCreated: (Category) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }



    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedCategory?.name ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(UiText.StringResourceId(Res.string.category_label).asString()) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            leadingIcon = {
                selectedCategory?.let {
                    CategoryIcon(selectedCategory.icon)
                }
            },
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
            .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = category.name,
                            modifier = Modifier.padding(start = 8.dp),
                            fontWeight = FontWeight.Medium
                        )
                    },
                    leadingIcon = {
                        CategoryIcon(category.icon)
                    },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }


            DropdownMenuItem(
                text = {
                    Text(
                        text = UiText.StringResourceId(Res.string.add_new_category).asString(),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
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
    var selectedEmoji by remember { mutableStateOf(emojiOptions.first()) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text( UiText.StringResourceId(Res.string.create_new_category_title).asString()) },
            text = {
                Column {
                    OutlinedTextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        label = { Text(UiText.StringResourceId(Res.string.category_name_label).asString()) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))
                    Column(
                        Modifier.height(256.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            emojiOptions.forEach { emoji ->
                                Text(
                                    text = emoji,
                                    fontSize = 24.sp,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(if (emoji == selectedEmoji) AppColors.primary.copy(0.2f) else Color.Transparent)
                                        .clickable { selectedEmoji = emoji }
                                        .padding(8.dp)
                                )
                            }
                        }
                    }

                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newCategoryName.isNotBlank()) {
                        val newCategory = Category(
                            name = newCategoryName,
                            icon = selectedEmoji,
                            type = CategoryType.WISHLIST
                        )
                        onNewCategoryCreated(newCategory)
                        onCategorySelected(newCategory)
                        showDialog = false
                        newCategoryName = ""
                    }
                }) {
                    Text(UiText.StringResourceId(Res.string.create_button).asString())
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
