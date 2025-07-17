package com.yusufteker.worthy.core.presentation

import kotlin.math.PI
import kotlin.math.absoluteValue

fun Double.toRadians(): Double = this * PI / 180


fun Float.formatTwoDecimals(): String {
    val intPart = this.toInt()
    val decimalPart = ((this - intPart) * 100).toInt().absoluteValue
    return "$intPart.${decimalPart.toString().padStart(2, '0')}"
}

fun Double.formatTwoDecimals(): String {
    val intPart = this.toInt()
    val decimalPart = ((this - intPart) * 100).toInt().absoluteValue
    return "$intPart.${decimalPart.toString().padStart(2, '0')}"
}

