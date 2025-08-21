package com.yusufteker.worthy.core.presentation

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: UiText? = null
)
