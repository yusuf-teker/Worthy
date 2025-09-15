package com.yusufteker.worthy.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.yusufteker.worthy.core.data.database.db.DatabaseFactory
import com.yusufteker.worthy.core.data.network.util.NetworkStatus
import com.yusufteker.worthy.core.media.ImageSaver
import com.yusufteker.worthy.screen.onboarding.domain.OnboardingManager
import com.yusufteker.worthy.screen.settings.data.createDataStore
import com.yusufteker.worthy.screen.settings.domain.UserPrefsManager
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

actual val platformModule: Module
    get() = module {
        single<DataStore<Preferences>> {
            createDataStore(androidApplication())
        }
        single { UserPrefsManager(get()) }
        single { OnboardingManager(get()) }

        single { DatabaseFactory(androidApplication()) }

        single { ImageSaver(androidApplication()) }

        single<HttpClientEngine> {  OkHttp.create {
            config {
                connectTimeout(2, TimeUnit.SECONDS)
                readTimeout(2, TimeUnit.SECONDS)
                writeTimeout(2, TimeUnit.SECONDS)
            }
        }}

        single { NetworkStatus(androidApplication()) }
    }