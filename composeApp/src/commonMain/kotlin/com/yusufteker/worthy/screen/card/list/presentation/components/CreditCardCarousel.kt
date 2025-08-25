package com.yusufteker.worthy.screen.card.list.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.yusufteker.worthy.screen.card.domain.model.Card
import com.yusufteker.worthy.screen.card.domain.model.CardBrand
import com.yusufteker.worthy.core.presentation.util.groupEvery4
import com.yusufteker.worthy.screen.card.add.presentation.components.CreditCardPreview
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreditCardCarousel(
    cards: List<Card>, // kendi modelin, içinde holder, number, expiry vs
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        initialPage = cards.size-1, // burada değiştirebilirsin
        pageCount = { cards.size }
    )

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxWidth(),
        pageSpacing = 16.dp,
        contentPadding = PaddingValues(horizontal = 48.dp),
        beyondViewportPageCount = 2,
        reverseLayout = true,
        snapPosition = SnapPosition.Center // ortalama


    ) { page ->

        val pageOffset = (pagerState.currentPage - page) +
                pagerState.currentPageOffsetFraction

        val scale = 1f - 0.15f * pageOffset.absoluteValue
        val rotation = 8f * pageOffset

        CreditCardPreview(
            cardHolder =  cards[page].cardHolderName,
            cardNumberFormatted = groupEvery4(cards[page].cardNumber),
            expiryFormatted = "${cards[page].expiryMonth}/ ${cards[page].expiryYear}",
            brand = cards[page].cardBrand ?: CardBrand.Unknown,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.586f)
                .graphicsLayer {
                    scaleX = scale.coerceAtLeast(minimumValue = 0.65f)
                    scaleY = scale.coerceAtLeast(minimumValue = 0.65f)
                    rotationY = rotation
                }
        )
    }
}


@Preview()
@Composable
fun CreditCardCarouselPreview() {

    val dummyCards = listOf(
        Card(
            id = 1,
            cardHolderName = "YUSUF TEKER",
            cardNumber = "1234567812345678",
            expiryMonth = 12,
            expiryYear = 25,
            cardBrand = CardBrand.Visa,
            cvv = "123"
        ),
        Card(
            id = 2,
            cardHolderName = "JOHN DOE",
            cardNumber = "8765432187654321",
            expiryMonth = 5,
            expiryYear = 27,
            cardBrand = CardBrand.Mastercard,
            cvv = "233"
        ),
        Card(
            id = 3,
            cardHolderName = "JANE DOE",
            cardNumber = "1111222233334444",
            expiryMonth = 1,
            expiryYear = 30,
            cardBrand = CardBrand.Unknown,
            cvv = "213"
        )
    )

    Column(Modifier.fillMaxWidth()) {
        CreditCardCarousel(
            cards = dummyCards,
            modifier = Modifier.fillMaxWidth()
        )
    }

}
