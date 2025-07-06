package com.yusufteker.worthy.feature.onboarding.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.io.IOException

class OnboardingManager(private val dataStore: DataStore<Preferences>) {

    private companion object {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    /** Tek seferlik okuma */
    suspend fun isOnboardingCompleted(): Boolean =
        dataStore.data.first()[ONBOARDING_COMPLETED] ?: false

    /** Kaydetme */
    suspend fun setOnboardingCompleted(completed: Boolean = true) =
        dataStore.edit { it[ONBOARDING_COMPLETED] = completed }

    suspend fun resetOnboarding() {
        setOnboardingCompleted(false)
    }
}