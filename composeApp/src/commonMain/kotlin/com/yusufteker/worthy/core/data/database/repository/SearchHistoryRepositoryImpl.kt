package com.yusufteker.worthy.core.data.database.repository

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.yusufteker.worthy.core.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class SearchHistoryRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : SearchHistoryRepository {

    companion object {
        private val SEARCH_HISTORY_KEY = stringPreferencesKey("search_history")
        private const val MAX_HISTORY_SIZE = 10
    }

    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    override val searchHistory: Flow<List<String>> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { prefs ->
            val jsonString = prefs[SEARCH_HISTORY_KEY]
            if (jsonString.isNullOrEmpty()) {
                emptyList()
            } else {
                try {
                    json.decodeFromString<List<String>>(jsonString)
                } catch (e: Exception) {
                    emptyList()
                }
            }
        }

    override suspend fun addSearchQuery(query: String) {
        if (query.isBlank()) return

        dataStore.edit { prefs ->
            val currentList = prefs[SEARCH_HISTORY_KEY]?.let {
                try {
                    json.decodeFromString<List<String>>(it).toMutableList()
                } catch (e: Exception) {
                    mutableListOf()
                }
            } ?: mutableListOf()

            // Tekrar eden varsa sil, sonra en başa ekle
            currentList.remove(query)
            currentList.add(0, query)

            // Maksimum uzunluk sınırı
            if (currentList.size > MAX_HISTORY_SIZE) {
                currentList.removeLast()
            }


            prefs[SEARCH_HISTORY_KEY] = json.encodeToString(currentList)
        }
    }

    override suspend fun clearHistory() {
        dataStore.edit { prefs ->
            prefs.remove(SEARCH_HISTORY_KEY)
        }
    }
}
