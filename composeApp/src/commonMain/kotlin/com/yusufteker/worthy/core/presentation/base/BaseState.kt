package com.yusufteker.worthy.core.presentation.base

interface BaseState{
    val isLoading: Boolean
    fun copyWithLoading(isLoading: Boolean): BaseState

}
