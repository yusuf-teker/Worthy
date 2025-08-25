package com.yusufteker.worthy.core.presentation.util

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.yusufteker.worthy.screen.card.domain.model.CardBrand
import kotlin.math.min


public fun detectCardBrand(digits: String): CardBrand {
    if (digits.isEmpty()) return CardBrand.Unknown
    return when {
        digits.startsWith("4") -> CardBrand.Visa
        // Mastercard BIN aralığı (51–55) veya 2221–2720
        digits.take(2).toIntOrNull() in 51..55 ||
                digits.take(4).toIntOrNull() in 2221..2720 -> CardBrand.Mastercard
        // TROY BIN → 9792 ile başlar
        digits.startsWith("9792") -> CardBrand.Troy
        else -> CardBrand.Unknown
    }
}


 fun groupEvery4(raw: String): String =
    raw.chunked(4).joinToString(" ")

 fun parseExpiry(raw: String): Pair<String, String> {
    val mm = raw.take(2)
    val yy = raw.drop(2).take(2)
    return mm to yy
}

/* ---------- Visual Transformations ---------- */

 object CardNumberGroupingTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text.filter { it.isDigit() }
        val formatted = groupEvery4(raw)
        val mapping = object : OffsetMapping {
            // raw -> formatted
            override fun originalToTransformed(offset: Int): Int {
                val groupsBefore = (offset / 4) //imleci bosluk kadar sağa kaydır
                // formatted formatlı boşluklı string 1234 56
                // offset raw stringin uzunlugu "123456".length -> 5
                // groupsBefore -> 5/4 -> 1
                // imlec formatlı stringin 6.karakterin sağında olacak 6|
                // formatlı stringin uzunlugundan daha fazlal olamaz cunku zaten formatlı stringin sonuna gelecek
                // 1234 durumunda offset 4 group 1 oldugu icin min aldık yoksa 5 oluyor
                return min(formatted.length, offset + groupsBefore)
            }
            // formatted -> raw
            override fun transformedToOriginal(offset: Int): Int {
                var rawCount = 0
                var i = 0
                while (i < offset && i < formatted.length) {
                    if (formatted[i] != ' ') rawCount++
                    i++
                }
                return rawCount
            }
        }
        return TransformedText(AnnotatedString(formatted), mapping)
    }
}

 object ExpiryVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.filter { it.isDigit() }.take(4)
        val formatted = when (digits.length) {
            0 -> ""
            in 1..2 -> digits
            else -> digits.substring(0, 2) + "/" + digits.substring(2)
        }
        val mapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int =
                if (offset <= 2) offset else offset + 1
            override fun transformedToOriginal(offset: Int): Int =
                if (offset <= 2) offset else (offset - 1)
        }
        return TransformedText(AnnotatedString(formatted), mapping)
    }
}

/* ---------- KeyboardOptions defaults (KMP friendly) ---------- */

object KeyboardOptionsDefaults {
    val Number = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
    val NumberPassword = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.NumberPassword)
}

object CardValidator {

    fun isFormValid(
        showCardDetailInputs: Boolean,
        holder: String,
        number: String,
        expiry: String,
        cvv: String,
        nickname: String
    ): Boolean {
        return if (showCardDetailInputs) {
            holder.isNotBlank() &&
                    number.length == 16 &&
                    expiry.length == 4 &&
                    cvv.length in 3..4
        } else {
            nickname.isNotBlank()
        }
    }
}