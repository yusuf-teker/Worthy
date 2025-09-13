package com.yusufteker.worthy.screen.wishlist.add.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.domain.getCurrentLocalDateTime
import com.yusufteker.worthy.core.domain.model.CategoryType

import com.yusufteker.worthy.core.domain.repository.CategoryRepository
import com.yusufteker.worthy.core.domain.toEpochMillis
import com.yusufteker.worthy.core.media.ImageSaver
import com.yusufteker.worthy.core.media.toByteArray
import com.yusufteker.worthy.core.presentation.UiEvent
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.core.presentation.util.emptyMoney
import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WishlistAddViewModel(
    private val imageSaver: ImageSaver,
    private val wishlistRepository: WishlistRepository,
    private val categoryRepository: CategoryRepository,
) : BaseViewModel<WishlistAddState>(WishlistAddState()) {


    init {
        observeCategory()
    }

    fun onAction(action: WishlistAddAction) {
        when (action) {

            is WishlistAddAction.OnImageSelected -> {

                _state.value = _state.value.copy(
                    imageBitmap = action.platformImage
                )
            }

            WishlistAddAction.OnWishlistAdd -> {
                saveWishlistItem()
            }

            is WishlistAddAction.OnCategorySelected -> {
                _state.value = _state.value.copy(
                    wishlistItem = _state.value.wishlistItem.copy(
                        category = action.wishlistCategory
                    )
                )
            }

            is WishlistAddAction.OnNameChanged -> {
                _state.value = _state.value.copy(
                    wishlistItem = _state.value.wishlistItem.copy(
                        name = action.name
                    )
                )

            }

            is WishlistAddAction.OnNoteChanged -> {
                _state.value = _state.value.copy(
                    wishlistItem = _state.value.wishlistItem.copy(
                        note = action.note
                    )
                )
            }

            is WishlistAddAction.OnPriceChanged -> {
                _state.value = _state.value.copy(
                    wishlistItem = _state.value.wishlistItem.copy(
                        price = action.price ?: emptyMoney()
                    )
                )
            }

            is WishlistAddAction.OnPriorityChanged -> {
                _state.value = _state.value.copy(
                    wishlistItem = _state.value.wishlistItem.copy(
                        priority = action.priority
                    )
                )
            }

            is WishlistAddAction.OnPurchasedChanged -> {
                _state.value = _state.value.copy(
                    wishlistItem = _state.value.wishlistItem.copy(
                        isPurchased = action.isPurchased,
                        purchasedDate = getCurrentLocalDateTime().toEpochMillis()
                    )
                )
            }

            WishlistAddAction.OnSaveClicked -> {

                saveWishlistItem()

            }

            is WishlistAddAction.OnNewCategoryCreated -> {
                viewModelScope.launch {
                    val categoryId = categoryRepository.insert(action.wishlistCategory)
                    _state.update { currentState ->
                        currentState.copy(
                            wishlistItem = state.value.wishlistItem.copy(
                                category = action.wishlistCategory.copy(
                                    id = categoryId.toInt()
                                )
                            )
                        )

                    }
                }
            }

            WishlistAddAction.OnBackClick -> {
                navigateBack()
            }
        }
    }

    fun saveWishlistItem() {
        launchWithLoading {
            val wishlistItem = _state.value.wishlistItem.copy(

                imageUri = state.value.imageBitmap?.toByteArray()?.let { byteArray ->
                    imageSaver.saveImage(byteArray)
                }
            )
            wishlistRepository.insert(wishlistItem)
            navigateBack()

        }
    }

    private fun observeCategory() {
        launchWithLoading {

            categoryRepository.getAll()
                .onEach { categories ->
                    val wishlistCategories = categories.filter { it.type == CategoryType.WISHLIST }

                    _state.update { currentState ->
                        val shouldUpdateCategory = currentState.wishlistItem.category == null
                        val defaultCategory = wishlistCategories.firstOrNull()

                        currentState.copy(
                            wishlistCategories = wishlistCategories,
                            wishlistItem = if (shouldUpdateCategory && defaultCategory != null) {
                                currentState.wishlistItem.copy(category = defaultCategory)
                            } else {
                                currentState.wishlistItem
                            }
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }

}