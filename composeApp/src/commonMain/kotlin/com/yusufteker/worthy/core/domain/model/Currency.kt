package com.yusufteker.worthy.core.domain.model

import com.yusufteker.worthy.core.presentation.UiText
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.currency_aud
import worthy.composeapp.generated.resources.currency_cad
import worthy.composeapp.generated.resources.currency_chf
import worthy.composeapp.generated.resources.currency_cny
import worthy.composeapp.generated.resources.currency_eur
import worthy.composeapp.generated.resources.currency_gbp
import worthy.composeapp.generated.resources.currency_jpy
import worthy.composeapp.generated.resources.currency_nzd
import worthy.composeapp.generated.resources.currency_sek
import worthy.composeapp.generated.resources.currency_try
import worthy.composeapp.generated.resources.currency_usd

enum class Currency(val symbol: String, val label: UiText) {

    TRY("₺", UiText.StringResourceId(Res.string.currency_try)),
    USD("$", UiText.StringResourceId(Res.string.currency_usd)),
    EUR("€", UiText.StringResourceId(Res.string.currency_eur)),
    GBP("£", UiText.StringResourceId(Res.string.currency_gbp)),
    JPY("¥", UiText.StringResourceId(Res.string.currency_jpy)),
    AUD("A$", UiText.StringResourceId(Res.string.currency_aud)),
    CAD("C$", UiText.StringResourceId(Res.string.currency_cad)),
    CHF("CHF", UiText.StringResourceId(Res.string.currency_chf)),
    CNY("¥", UiText.StringResourceId(Res.string.currency_cny)),
    SEK("kr", UiText.StringResourceId(Res.string.currency_sek)),
    NZD("NZ$", UiText.StringResourceId(Res.string.currency_nzd)),
    // APIDE YOK Şimdilik kaldırıldı //GRAM_GOLD("GR", UiText.StringResourceId(Res.string.currency_gram_gold)),
    // APIDE YOK //BTC("₿", UiText.StringResourceId(Res.string.currency_btc))

}
