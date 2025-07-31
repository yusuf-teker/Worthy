package com.yusufteker.worthy.screen.wishlist.add.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.core.domain.model.CategoryType
import com.yusufteker.worthy.core.domain.model.startDate
import com.yusufteker.worthy.core.domain.repository.CategoryRepository
import com.yusufteker.worthy.core.media.ImageSaver
import com.yusufteker.worthy.core.media.toByteArray
import com.yusufteker.worthy.core.presentation.BaseViewModel
import com.yusufteker.worthy.screen.wishlist.list.domain.WishlistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WishlistAddViewModel(
    private val imageSaver: ImageSaver,
    private val wishlistRepository: WishlistRepository,
    private val categoryRepository: CategoryRepository,
    ) : BaseViewModel() {
    private val _state = MutableStateFlow(WishlistAddState())
    val state: StateFlow<WishlistAddState> = _state


    init {
        observeCategory()
    }

    fun onAction(action: WishlistAddAction) {
        when (action) {


            is WishlistAddAction.OnImageSelected -> {

                _state.value = _state.value.copy(
                    imageBitmap = action.bitmap
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
            is WishlistAddAction.OnPriceChanged ->  {
                _state.value = _state.value.copy(
                    wishlistItem = _state.value.wishlistItem.copy(
                        price = action.price
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
                        isPurchased = action.isPurchased
                    )
                )
            }
            WishlistAddAction.OnSaveClicked -> {

            }

            is WishlistAddAction.OnNewCategoryCreated -> {
                viewModelScope.launch {
                    categoryRepository.insert(action.wishlistCategory)
                }
            }
        }
    }

     fun saveWishlistItem() {

         viewModelScope.launch {
             state.value.imageBitmap?.toByteArray()?.let { byteArray ->
                 imageSaver.saveImage(byteArray)
             }
         }
    }

    private fun observeCategory() {

        categoryRepository.getAll()
            .onEach { categories ->
                _state.update { currentState ->
                    currentState.copy(
                        wishlistCategories = categories.filter { it.type == CategoryType.WISHLIST }
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}