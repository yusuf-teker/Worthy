package com.yusufteker.worthy.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null){
    startKoin {
        config?.invoke(this) // varsa configleri kullan
        modules(sharedModule, platformModule) // kullanilacak modüller
    }
}