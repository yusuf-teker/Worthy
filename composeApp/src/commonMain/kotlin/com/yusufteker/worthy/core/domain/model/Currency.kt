package com.yusufteker.worthy.core.domain.model

import com.yusufteker.worthy.core.presentation.UiText
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.currency_btc
import worthy.composeapp.generated.resources.currency_eur
import worthy.composeapp.generated.resources.currency_gbp
import worthy.composeapp.generated.resources.currency_gram_gold
import worthy.composeapp.generated.resources.currency_try
import worthy.composeapp.generated.resources.currency_usd

enum class Currency(val symbol: String, val label: UiText) {
    TRY("₺", UiText.StringResourceId(Res.string.currency_try)),
    USD("$", UiText.StringResourceId(Res.string.currency_usd)),
    EUR("€", UiText.StringResourceId(Res.string.currency_eur)),
    GBP("£", UiText.StringResourceId(Res.string.currency_gbp)),
    GRAM_GOLD("GR", UiText.StringResourceId(Res.string.currency_gram_gold)),
    BTC("₿", UiText.StringResourceId(Res.string.currency_btc))
}
