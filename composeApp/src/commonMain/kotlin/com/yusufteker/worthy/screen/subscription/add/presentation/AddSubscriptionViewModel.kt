package com.yusufteker.worthy.screen.subscription.add.presentation

import androidx.lifecycle.viewModelScope
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.BaseViewModel
import com.yusufteker.worthy.screen.settings.domain.UserPrefsManager
import com.yusufteker.worthy.screen.subscription.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.error_empty_category
import worthy.composeapp.generated.resources.error_empty_price
import worthy.composeapp.generated.resources.error_empty_service_name

class AddSubscriptionViewModel(
    private val subscriptionRepository: SubscriptionRepository,
    private val userPrefsManager: UserPrefsManager
) : BaseViewModel<AddSubscriptionState>(AddSubscriptionState()) {

//TODO VALIDATION LAR EKLENECEK

    init {

        viewModelScope.launch {
            userPrefsManager.isSubscriptionCategoryInitialized.collect { initialized ->
                if (!initialized) {
                    subscriptionRepository.initializeDefaultCategories()
                    userPrefsManager.setSubscriptionCategoryInitialized(true)
                }
            }
        }
        observeData()

    }

    fun observeData() {

        launchWithLoading {
            combine(
                subscriptionRepository.getCards(),
                subscriptionRepository.getCategories(),
            ) { cards, categories ->
                _state.update { currentState ->
                    currentState.copy(
                        cards = cards, categories = categories
                    )
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
                        subscriptionPrev = state.value.subscriptionPrev.copy(name = action.name),
                        errorName = null
                    )
                }
            }

            is AddSubscriptionAction.OnCategorySelected -> {
                _state.update {
                    it.copy(
                        selectedCategory = action.category,
                        subscriptionPrev = state.value.subscriptionPrev.copy(
                            category = action.category, icon = action.category?.icon ?: ""
                        ),
                        errorCategory = null

                    )
                }
            }

            is AddSubscriptionAction.OnPriceChanged -> {
                _state.update {
                    it.copy(
                        price = action.price,
                        subscriptionPrev = state.value.subscriptionPrev.copy(amount = action.price),
                        errorPrice = null
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
                launchWithLoading {
                    subscriptionRepository.addCategory(action.category)
                }
            }

            is AddSubscriptionAction.OnColorChanged -> {
                _state.update {
                    it.copy(
                        color = action.color,
                        subscriptionPrev = state.value.subscriptionPrev.copy(colorHex = action.color)
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

            is AddSubscriptionAction.AddNewCardClicked -> {
                navigateTo(Routes.AddCard)
            }

        }
    }

    private fun submitSubscription() {
        // validation ve kaydetme işlemleri
        launchWithLoading {
            val state = state.value
            val validated = validate(state)
            if (validated.hasError()) {
                _state.value = validated
            } else {
                subscriptionRepository.addSubscription(state.subscriptionPrev)

                navigateBack()

            }

        }
    }

    private fun validate(state: AddSubscriptionState): AddSubscriptionState {
        return state.copy(
            errorName = if (state.subscriptionName.isBlank()) UiText.StringResourceId(Res.string.error_empty_service_name) else null,
            errorCategory = if (state.selectedCategory == null) UiText.StringResourceId(Res.string.error_empty_category) else null,
            errorPrice = if (state.price == null || state.price.amount <= 0) UiText.StringResourceId(
                Res.string.error_empty_price
            ) else null,
        )
    }

}

