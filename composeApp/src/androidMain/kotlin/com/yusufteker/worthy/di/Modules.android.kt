package com.yusufteker.worthy.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.yusufteker.worthy.core.data.database.db.DatabaseFactory
import com.yusufteker.worthy.screen.onboarding.domain.OnboardingManager
import com.yusufteker.worthy.screen.settings.data.createDataStore
import com.yusufteker.worthy.screen.settings.domain.UserPrefsManager

import org.koin.android.ext.koin.androidApplication

actual val platformModule: Module
    get() = module {
        single<DataStore<Preferences>> {
            createDataStore(androidApplication())
        }
        single { UserPrefsManager(get()) }
        single { OnboardingManager(get()) }

        single { DatabaseFactory(androidApplication()) }


        single<HttpClientEngine> { OkHttp.create() } // Platform spesifik dependency
    }