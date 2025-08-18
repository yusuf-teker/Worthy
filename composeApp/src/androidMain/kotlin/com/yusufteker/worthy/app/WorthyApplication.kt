package com.yusufteker.worthy.app

import android.app.Application
import android.content.Context
import com.yusufteker.worthy.di.initKoin
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext

class WorthyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog()) // init logger
        initKoin {
            androidContext(this@WorthyApplication)
        }
        AndroidApplicationContext.init(this)

    }
}

object AndroidApplicationContext {
    lateinit var appContext: Context
        private set

    fun init(context: Context) {
        appContext = context
    }
}