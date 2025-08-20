package com.yusufteker.worthy.screen.card.add.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.app.navigation.NavigationHandler
import com.yusufteker.worthy.app.navigation.Routes
import com.yusufteker.worthy.core.domain.model.Card
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
import com.yusufteker.worthy.core.presentation.components.AppTopBar
import com.yusufteker.worthy.core.presentation.components.DayOfMonthSelector
import com.yusufteker.worthy.core.presentation.components.ErrorText
import com.yusufteker.worthy.core.presentation.util.CardNumberGroupingTransformation
import com.yusufteker.worthy.core.presentation.util.CardValidator
import com.yusufteker.worthy.core.presentation.util.ExpiryVisualTransformation
import com.yusufteker.worthy.core.presentation.util.KeyboardOptionsDefaults
import com.yusufteker.worthy.core.presentation.util.detectCardBrand
import com.yusufteker.worthy.core.presentation.util.groupEvery4
import com.yusufteker.worthy.core.presentation.util.parseExpiry
import com.yusufteker.worthy.screen.card.add.presentation.components.CreditCardPreview
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.card_expiry_label
import worthy.composeapp.generated.resources.card_nickname_label
import worthy.composeapp.generated.resources.card_nickname_placeholder
import worthy.composeapp.generated.resources.card_number
import worthy.composeapp.generated.resources.cardholder_label
import worthy.composeapp.generated.resources.cardholder_placeholder
import worthy.composeapp.generated.resources.error_card_number_invalid
import worthy.composeapp.generated.resources.error_cvv_invalid
import worthy.composeapp.generated.resources.error_expiry_month_invalid
import worthy.composeapp.generated.resources.expiry_month_error
import worthy.composeapp.generated.resources.expiry_placeholder
import worthy.composeapp.generated.resources.save_card
import worthy.composeapp.generated.resources.screen_title_add_new_card
import worthy.composeapp.generated.resources.toggle_card_details_show
import worthy.composeapp.generated.resources.toggle_card_details_skip

@Composable
fun AddCardScreenRoot(
    viewModel: AddCardViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    onNavigateTo: (Routes) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NavigationHandler(viewModel){ route, data ->
        onNavigateTo(route)
    }
    BaseContentWrapper(
        state = state
    ) {
        AddCardScreen(
            state = state, onAction = viewModel::onAction, contentPadding = contentPadding
        )
    }
}

@Composable
fun AddCardScreen(
    state: AddCardState,
    onAction: (action: AddCardAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
) {

    var nickname by remember { mutableStateOf("") }
    var holder by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }          // raw digits
    var expiry by remember { mutableStateOf("") }          // raw digits MMYY
    var cvv by remember { mutableStateOf("") }
    var statementDay by remember { mutableStateOf<Int?>(1) }

    val cardBrand = remember(number) { detectCardBrand(number) }
    val formattedNumber = remember(number) { groupEvery4(number) }
    val (mm, yy) = remember(expiry) { parseExpiry(expiry) }

    var showCardDetailInputs by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize().padding(contentPadding), topBar = {
            AppTopBar(
                title = UiText.StringResourceId(Res.string.screen_title_add_new_card).asString(),
                onNavIconClick = {
                    onAction(AddCardAction.OnNavigateBack)
                })
        }) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier.fillMaxSize().padding(contentPadding).verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            CreditCardPreview(
                cardHolder = holder,
                cardNumberFormatted = formattedNumber.ifEmpty { "#### #### #### ####" },
                expiryFormatted = if (mm.isNotEmpty() || yy.isNotEmpty()) "${
                    mm.padStart(
                        2, '0'
                    )
                }/${yy.take(2)}" else UiText.StringResourceId(Res.string.expiry_placeholder)
                    .asString(),
                brand = cardBrand,
                modifier = Modifier.fillMaxWidth().aspectRatio(1.58f) // yaklaşık kredi kartı oranı
            )

            // Nickname
            var nicknameError by remember { mutableStateOf<UiText?>(null) }
            OutlinedTextField(
                value = nickname,
                onValueChange = { input ->
                    nickname = input
                },
                label = {
                    Text(
                        UiText.StringResourceId(Res.string.card_nickname_label).asString()
                    )
                },
                placeholder = {
                    Text(
                        UiText.StringResourceId(Res.string.card_nickname_placeholder).asString()
                    )
                },
                singleLine = true,
                isError = nicknameError != null,
                modifier = Modifier.fillMaxWidth()
            )
            val toggleText = if (showCardDetailInputs) {
                UiText.StringResourceId(Res.string.toggle_card_details_skip).asString()
            } else {
                UiText.StringResourceId(Res.string.toggle_card_details_show).asString()
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = toggleText, modifier = Modifier.weight(1f)
                )

                val rotation by animateFloatAsState(
                    if (showCardDetailInputs) 180f else 0f, label = "arrowAnim"
                )

                IconButton(onClick = { showCardDetailInputs = !showCardDetailInputs }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.rotate(rotation)
                    )
                }
            }


            AnimatedVisibility(
                visible = showCardDetailInputs,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Kart Sahibi
                    var holderError by remember { mutableStateOf<UiText?>(null) }
                    var wasHolderFocused by remember { mutableStateOf(false) }

                    OutlinedTextField(
                        value = holder,
                        onValueChange = { input ->
                            holder = input.uppercase()
                            holderError = null
                        },
                        label = {
                            Text(
                                UiText.StringResourceId(Res.string.cardholder_label).asString()
                            )
                        },
                        placeholder = {
                            Text(
                                UiText.StringResourceId(Res.string.cardholder_placeholder)
                                    .asString()
                            )
                        },
                        singleLine = true,
                        isError = holderError != null,
                        modifier = Modifier.fillMaxWidth().onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                wasHolderFocused = true
                            } else if (wasHolderFocused) {
                                holderError = if (holder.isBlank()) {
                                    //UiText.StringResourceId(Res.string.error_cardholder_empty)
                                    null
                                } else if (holder.split(" ").size > 3) {
                                    null
                                } else null
                            }
                        })
                    ErrorText(holderError?.asString())

                    // Kart Numarası
                    var cardNumberError by remember { mutableStateOf<UiText?>(null) }
                    var wasCardNumberFocused by remember { mutableStateOf(false) }

                    OutlinedTextField(
                        value = number,
                        onValueChange = { input ->
                            val digits = input.filter { it.isDigit() }.take(16)
                            number = digits
                            cardNumberError = null
                        },
                        isError = cardNumberError != null,
                        label = {
                            Text(
                                UiText.StringResourceId(Res.string.card_number).asString()
                            )
                        },
                        placeholder = { Text("1234 5678 9012 3456") },
                        singleLine = true,
                        visualTransformation = CardNumberGroupingTransformation,
                        keyboardOptions = KeyboardOptionsDefaults.Number,
                        modifier = Modifier.fillMaxWidth().onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                wasCardNumberFocused = true
                            } else if (wasCardNumberFocused) {
                                cardNumberError = if (number.isEmpty()) {
                                    //UiText.StringResourceId(Res.string.error_card_number_empty)
                                    null//todo empty durumu burada kontrol etmek istemedim
                                } else if (number.length < 16) {
                                    UiText.StringResourceId(Res.string.error_card_number_invalid)
                                } else null
                            }
                        })
                    ErrorText(cardNumberError?.asString())

                    // Son Kullanma (MM/YY)
                    var expiryErrorMessage by remember { mutableStateOf<UiText?>(null) }
                    var wasExpiryFocused by remember { mutableStateOf(false) }

                    OutlinedTextField(
                        value = expiry,
                        onValueChange = { input ->
                            val digits = input.filter { it.isDigit() }.take(4)

                            expiryErrorMessage = if ((digits.take(2).toIntOrNull() ?: 0) > 12) {
                                UiText.StringResourceId(Res.string.expiry_month_error)
                            } else {
                                null
                            }

                            expiry = digits
                        },
                        isError = expiryErrorMessage != null,
                        label = {
                            Text(
                                UiText.StringResourceId(Res.string.card_expiry_label).asString()
                            )
                        },
                        placeholder = {
                            Text(
                                UiText.StringResourceId(Res.string.expiry_placeholder).asString()
                            )
                        },
                        singleLine = true,
                        visualTransformation = ExpiryVisualTransformation,
                        keyboardOptions = KeyboardOptionsDefaults.Number,
                        modifier = Modifier.fillMaxWidth().onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                wasExpiryFocused = true
                            } else if (wasExpiryFocused) {
                                expiryErrorMessage = if (expiry.isEmpty()) {
                                    //UiText.StringResourceId(Res.string.error_expiry_empty)
                                    null//todo empty durumu burada kontrol etmek istemedim
                                } else if ((expiry.take(2).toIntOrNull() ?: 0) > 12) {
                                    UiText.StringResourceId(Res.string.error_expiry_month_invalid)
                                } else null
                            }
                        })
                    ErrorText(expiryErrorMessage?.asString())

                    // CVV
                    var cvvErrorMessage by remember { mutableStateOf<UiText?>(null) }
                    var wasCvvFocused by remember { mutableStateOf(false) }

                    OutlinedTextField(
                        value = cvv,
                        onValueChange = { input ->
                            val digits = input.filter { it.isDigit() }.take(4) // 3–4 arası olabilir
                            cvv = digits
                        },
                        isError = cvvErrorMessage != null,
                        label = { Text("CVV") },
                        placeholder = { Text("***") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptionsDefaults.NumberPassword,
                        modifier = Modifier.fillMaxWidth().onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                wasCvvFocused = true
                            } else if (wasCvvFocused) {
                                cvvErrorMessage = if (cvv.isEmpty()) {
                                    //UiText.StringResourceId(Res.string.error_cvv_empty)
                                    null//todo empty durumu burada kontrol etmek istemedim
                                } else if (cvv.length < 2 || cvv.length > 4) {
                                    UiText.StringResourceId(Res.string.error_cvv_invalid)
                                } else null
                            }
                        })
                    ErrorText(cvvErrorMessage?.asString())

                    DayOfMonthSelector(
                        selectedDay = statementDay,
                        onDayChange = { statementDay = it }
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    val month = mm.toIntOrNull() ?: 0
                    val year = ("20" + yy.take(2)).toIntOrNull() ?: 0
                    onAction(
                        AddCardAction.AddCard(
                            Card(
                                cardHolderName = holder.trim(),
                                cardNumber = number,
                                expiryMonth = month,
                                expiryYear = year,
                                cvv = cvv,
                                nickname = nickname,
                                cardBrand = cardBrand,
                                note = "",
                                statementDay = statementDay,

                                )
                        )
                    )
                }, enabled = CardValidator.isFormValid(
                    showCardDetailInputs = showCardDetailInputs,
                    holder = holder,
                    number = number,
                    expiry = expiry,
                    cvv = cvv,
                    nickname = nickname
                ), modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text(UiText.StringResourceId(Res.string.save_card).asString())
            }
        }
    }
}





