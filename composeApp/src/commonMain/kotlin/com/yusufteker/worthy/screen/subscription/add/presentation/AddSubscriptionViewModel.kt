package com.yusufteker.worthy.screen.subscription.add.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.domain.model.emojiOptions
import com.yusufteker.worthy.core.domain.model.emptyMoney
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.screen.subscription.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class AddSubscriptionViewModel(
    private val subscriptionRepository: SubscriptionRepository
) : BaseViewModel<AddSubscriptionState>(AddSubscriptionState()) {

//TODO VALIDATION LAR EKLENECEK


    init {
        observeData()
    }
    fun observeData(){
        launchWithLoading {
            subscriptionRepository.getCards() .onEach { cards ->
                _state.update { currentState ->
                    currentState.copy(cards = cards)
                }
            }.launchIn(viewModelScope)
        }

    }
    fun onAction(action: AddSubscriptionAction) {
        when (action) {
            is AddSubscriptionAction.Init -> {
                // TODO
            }

            is AddSubscriptionAction.OnNameChanged -> {
                _state.update {
                    it.copy(
                        subscriptionName = action.name,
                        subscriptionPrev = state.value.subscriptionPrev.copy(name = action.name)
                    )
                }
            }
            is AddSubscriptionAction.OnCategorySelected -> {
                _state.update {
                    it.copy(
                        selectedCategory = action.category,
                        subscriptionPrev = state.value.subscriptionPrev.copy(
                            category = action.category,
                            icon = action.category?.defaultIcon ?: ""
                        )

                    )
                }
            }
            is AddSubscriptionAction.OnCustomCategoryChanged -> {
                _state.update {
                    it.copy(
                        customCategoryName = action.name,
                        subscriptionPrev = state.value.subscriptionPrev.copy(customCategoryName = action.name)

                    )
                }
            }

            is AddSubscriptionAction.OnEmojiSelected -> {
                _state.update {
                    it.copy(
                        selectedEmoji = action.emoji,
                        subscriptionPrev = state.value.subscriptionPrev.copy(icon = action.emoji)

                    )
                }
            }
            is AddSubscriptionAction.OnPriceChanged -> {
                _state.update {
                    it.copy(
                        price = action.price,
                        subscriptionPrev = state.value.subscriptionPrev.copy(money = action.price)

                    )
                }
            }
            is AddSubscriptionAction.OnStartDateChanged -> {
                _state.update {
                    it.copy(
                        startDate = action.date,
                        subscriptionPrev = state.value.subscriptionPrev.copy(startDate = action.date)

                    )
                }
            }
            is AddSubscriptionAction.OnEndDateChanged -> {
                _state.update {
                    it.copy(
                        endDate = action.date,
                        subscriptionPrev = state.value.subscriptionPrev.copy(endDate = action.date)

                    )
                }
            }
            is AddSubscriptionAction.OnScheduledDayChanged -> {
                _state.update {
                    it.copy(
                        scheduledDay = action.day,
                        subscriptionPrev = state.value.subscriptionPrev.copy(scheduledDay = action.day)

                    )
                }
            }
            is AddSubscriptionAction.SubmitSubscription -> {
                submitSubscription()
            }

            is AddSubscriptionAction.NavigateBack -> {
                navigateBack()
            }
            is AddSubscriptionAction.OnNewCategoryCreated -> {

            }

            is AddSubscriptionAction.OnColorChanged -> {
                _state.update {
                    it.copy(
                        color = action.color,
                        subscriptionPrev = state.value.subscriptionPrev.copy(color = action.color)
                    )
                }
            }

            is AddSubscriptionAction.OnCardSelected -> {
                _state.update {
                    it.copy(
                        selectedCard = action.card,
                        subscriptionPrev = state.value.subscriptionPrev.copy(cardId = action.card.id)
                    )
                }
            }

        }
    }

    private fun submitSubscription() {
        // validation ve kaydetme işlemleri
        launchWithLoading {
            val state = state.value

            val error = when { // TODO ERROR HANDLING SONRA EKLENECEK
                state.subscriptionName.isBlank() -> "Abonelik adı boş olamaz"
                state.selectedCategory == null ->
                    "Bir kategori seçmelisiniz"
                state.price == emptyMoney() -> "Fiyat boş olamaz"
                state.startDate == null -> "Başlangıç tarihi seçilmelidir"
                state.endDate != null && state.startDate != null && state.endDate < state.startDate ->
                    "Bitiş tarihi başlangıç tarihinden önce olamaz"
                else -> null
            }

            if (error != null) {
                _state.update {
                    it.copy(errorMessage = error)
                }
                return@launchWithLoading
            }
            subscriptionRepository.addSubscription(state.subscriptionPrev)

            navigateBack()

        }
    }
}

