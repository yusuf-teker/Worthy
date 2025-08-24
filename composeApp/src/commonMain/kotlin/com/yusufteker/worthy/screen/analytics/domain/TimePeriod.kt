package com.yusufteker.worthy.screen.analytics.domain

import com.yusufteker.worthy.core.presentation.UiText
import worthy.composeapp.generated.resources.Res
import worthy.composeapp.generated.resources.period_month
import worthy.composeapp.generated.resources.period_none
import worthy.composeapp.generated.resources.period_six_months
import worthy.composeapp.generated.resources.period_three_months
import worthy.composeapp.generated.resources.period_week
import worthy.composeapp.generated.resources.period_year

enum class TimePeriod(val days: Int, val label: UiText) {

    NONE(0, UiText.StringResourceId(Res.string.period_none)),
    WEEK(7, UiText.StringResourceId(Res.string.period_week)),
    MONTH(30, UiText.StringResourceId(Res.string.period_month)),
    THREE_MONTHS(90, UiText.StringResourceId(Res.string.period_three_months)),
    SIX_MONTHS(180, UiText.StringResourceId(Res.string.period_six_months)),
    YEAR(365, UiText.StringResourceId(Res.string.period_year)),
}