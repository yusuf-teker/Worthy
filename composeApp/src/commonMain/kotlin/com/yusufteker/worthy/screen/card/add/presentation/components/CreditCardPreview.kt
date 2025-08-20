package com.yusufteker.worthy.screen.card.add.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusufteker.worthy.core.domain.model.CardBrand

@Composable
fun CreditCardPreview(
    cardHolder: String,
    cardNumberFormatted: String,
    expiryFormatted: String,
    brand: CardBrand,
    modifier: Modifier = Modifier
) {
    val gradient = when (brand) {
        CardBrand.Visa -> Brush.linearGradient(
            listOf(
                Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364)
            )
        )

        CardBrand.Mastercard -> Brush.linearGradient(listOf(Color(0xFF93291E), Color(0xFFED213A)))
        CardBrand.Unknown -> Brush.linearGradient(listOf(Color(0xFF1F1C2C), Color(0xFF928DAB)))
        CardBrand.Troy -> TODO()
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        shadowElevation = 6.dp,
        modifier = modifier
    ) {
        Box(
            Modifier.fillMaxSize().background(gradient).padding(20.dp)
        ) {
            // Brand
            Text(
                text = when (brand) {
                    CardBrand.Visa -> "VISA"
                    CardBrand.Mastercard -> "MASTERCARD"
                    CardBrand.Unknown -> "CARD"
                    CardBrand.Troy -> "TROY"
                },
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.TopEnd)
            )

            // Number
            Text(
                text = formatCardWithMask(cardNumberFormatted),
                color = Color.White,
                fontSize = 22.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.align(Alignment.CenterStart)
            )

            // Holder + Expiry
            Row(
                modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "CARDHOLDER",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 11.sp
                    )
                    Text(
                        text = cardHolder.ifEmpty { "NAME SURNAME" },
                        color = Color.White,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "EXPIRES", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp
                    )
                    Text(
                        text = expiryFormatted, color = Color.White, fontSize = 14.sp
                    )
                }
            }
        }
    }
}

fun formatCardWithMask(raw: String): String {
    val digits = raw.filter { it.isDigit() }
    val maxLength = 16

    // doldurulmamış yerleri # ile tamamla
    val masked = digits + "#".repeat(maxLength - digits.length)

    // 4’lü gruplara ayır
    return masked.chunked(4).joinToString(" ")
}