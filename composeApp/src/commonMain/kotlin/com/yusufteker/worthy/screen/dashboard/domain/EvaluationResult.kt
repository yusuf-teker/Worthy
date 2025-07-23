package com.yusufteker.worthy.screen.dashboard.domain

data class EvaluationResult(
    val incomePercent: Double,      // Gelirin yüzdesi
    val desirePercent: Double,      // İstek bütçesinin yüzdesi
    val workHours: Float,          // Karşılık gelen çalışma süresi
    val remainingDesire: Int,    // Kalan istek bütçesi
    val currencySymbol: String   // "₺" / "$" / "€" …
)
