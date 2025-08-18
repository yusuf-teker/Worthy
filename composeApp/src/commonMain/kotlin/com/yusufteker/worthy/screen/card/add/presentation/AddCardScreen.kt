package com.yusufteker.worthy.screen.card.add.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusufteker.worthy.core.domain.model.Card
import com.yusufteker.worthy.core.presentation.UiEvent
import com.yusufteker.worthy.core.presentation.UiText
import com.yusufteker.worthy.core.presentation.base.BaseContentWrapper
import com.yusufteker.worthy.core.presentation.components.AppTopBar
import com.yusufteker.worthy.core.presentation.util.CardNumberGroupingTransformation
import com.yusufteker.worthy.core.presentation.util.ExpiryVisualTransformation
import com.yusufteker.worthy.core.presentation.util.KeyboardOptionsDefaults
import com.yusufteker.worthy.core.presentation.util.detectCardBrand
import com.yusufteker.worthy.core.presentation.util.groupEvery4
import com.yusufteker.worthy.core.presentation.util.parseExpiry
import org.koin.compose.viewmodel.koinViewModel
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.add_new_card
import worthy.composeapp.generated.resources.screen_title_add_new_card
import com.yusufteker.worthy.screen.card.add.presentation.components.CreditCardPreview

@Composable
fun AddCardScreenRoot(
    viewModel: AddCardViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.NavigateBack -> {
                    onNavigateBack.invoke()
                }

                else -> Unit
            }
        }
    }
    BaseContentWrapper(
        state = state
    ) {
        AddCardScreen(state = state, onAction = viewModel::onAction, contentPadding = contentPadding)
    }
}

@Composable
fun AddCardScreen(
    state: AddCardState,
    onAction: (action: AddCardAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
) {

    var holder by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }          // raw digits
    var expiry by remember { mutableStateOf("") }          // raw digits MMYY
    var cvv by remember { mutableStateOf("") }

    val cardBrand = remember(number) { detectCardBrand(number) }
    val formattedNumber = remember(number) { groupEvery4(number) }
    val (mm, yy) = remember(expiry) { parseExpiry(expiry) }

    Scaffold(
        modifier = Modifier.fillMaxSize().padding(contentPadding),
        topBar = {
            AppTopBar(
                title = UiText.StringResourceId(Res.string.screen_title_add_new_card).asString(),
                onNavIconClick = {
                    onAction(AddCardAction.OnNavigateBack)
                }
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            CreditCardPreview(
                cardHolder = holder,
                cardNumberFormatted = formattedNumber.ifEmpty { "#### #### #### ####" },
                expiryFormatted = if (mm.isNotEmpty() || yy.isNotEmpty()) "${mm.padStart(2,'0')}/${yy.take(2)}" else "MM/YY",
                brand = cardBrand,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.58f) // yaklaşık kredi kartı oranı
            )

            // Kart Sahibi
            OutlinedTextField(
                value = holder,
                onValueChange = { input ->
                    holder = input.uppercase()
                },
                label = { Text("Cardholder Name") },
                placeholder = { Text("NAME SURNAME") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Kart Numarası
            OutlinedTextField(
                value = number,
                onValueChange = { input ->
                    val digits = input.filter { it.isDigit() }.take(19) // 19'a kadar bırak (AMEX gibi uzunlar için esnek)
                    number = digits
                },
                label = { Text("Card Number") },
                placeholder = { Text("1234 5678 9012 3456") },
                singleLine = true,
                visualTransformation = CardNumberGroupingTransformation,
                keyboardOptions = KeyboardOptionsDefaults.Number,
                modifier = Modifier.fillMaxWidth()
            )

            // Son Kullanma (MM/YY)
            OutlinedTextField(
                value = expiry,
                onValueChange = { input ->
                    val digits = input.filter { it.isDigit() }.take(4)
                    expiry = digits
                },
                label = { Text("Expiry (MM/YY)") },
                placeholder = { Text("MM/YY") },
                singleLine = true,
                visualTransformation = ExpiryVisualTransformation,
                keyboardOptions = KeyboardOptionsDefaults.Number,
                modifier = Modifier.fillMaxWidth()
            )

            // CVV
            OutlinedTextField(
                value = cvv,
                onValueChange = { input ->
                    val digits = input.filter { it.isDigit() }.take(4) // 3–4 arası olabilir
                    cvv = digits
                },
                label = { Text("CVV") },
                placeholder = { Text("***") },
                singleLine = true,
                keyboardOptions = KeyboardOptionsDefaults.NumberPassword,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    val month = mm.toIntOrNull() ?: 0
                    val year = ("20" + yy.take(2)).toIntOrNull() ?: 0
                    onAction(AddCardAction.AddCard(
                        Card(
                            cardHolderName = holder.trim(),
                            cardNumber = number,
                            expiryMonth = month,
                            expiryYear = year,
                            cvv = cvv,
                            nickname = "",
                            cardBrand = cardBrand,
                            note = "",
                            statementDay = 1,

                        )
                    ))
                },
                enabled = holder.isNotBlank() && number.length >= 13 && expiry.length == 4 && cvv.length >= 3,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("Save Card")
            }
        }
        }
    }





