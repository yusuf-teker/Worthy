package com.yusufteker.worthy.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.yusufteker.worthy.feature.onboarding.domain.OnboardingManager
import com.yusufteker.worthy.feature.settings.data.createDataStore
import com.yusufteker.worthy.feature.settings.domain.UserPrefsManager
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {

        single<DataStore<Preferences>> {
            createDataStore()
        }

        single { UserPrefsManager(get()) }
        single { OnboardingManager(get()) }


        single<HttpClientEngine> { Darwin.create() } // Platform spesifik dependency

    }