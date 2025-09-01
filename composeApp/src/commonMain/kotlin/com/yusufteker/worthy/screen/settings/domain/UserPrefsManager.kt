package com.yusufteker.worthy.screen.settings.domain

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.yusufteker.worthy.core.domain.model.Currency
import com.yusufteker.worthy.core.domain.model.Money
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class UserPrefsManager(private val dataStore: DataStore<Preferences>) {

    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private companion object {

        val USER_NAME = stringPreferencesKey("user_name")
        val INCOME_ITEMS = stringPreferencesKey("income_items")
        val EXPENSE_ITEMS = stringPreferencesKey("expense_items")
        val BUDGET_AMOUNT = stringPreferencesKey("budget_amount")
        val WEEKLY_WORK_HOURS = intPreferencesKey("weekly_work_hours")

        val CURRENCY = stringPreferencesKey("currency")

        val CURRENCY_RATES_JSON = stringPreferencesKey("currency_rates_json")
        val CURRENCY_RATES_TIMESTAMP = stringPreferencesKey("currency_rates_timestamp")

        val SPENDING_LIMIT = stringPreferencesKey("spending_limit")
        val SAVING_GOAL = stringPreferencesKey("saving_goal")

        val IS_CATEGORY_INITIALIZED = booleanPreferencesKey("is_category_initialized")
        val IS_SUBSCRIPTION_CATEGORY_INITIALIZED =
            booleanPreferencesKey("is_subscription_category_initialized")
    }

    /* ******** Flows ******** */
    /** GELİR */
    val incomeItems: Flow<List<IncomeItem>> = dataStore.data.catch { exception ->
        // IOException durumunda emptyPreferences() emit et
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { prefs ->
        try {
            prefs[INCOME_ITEMS]?.let { json.decodeFromString<List<IncomeItem>>(it) } ?: emptyList()
        } catch (e: SerializationException) {
            // Corrupt data durumunda boş liste döner
            emptyList()
        }
    }

    /** HAFTALIK ÇALIŞMA SAATİ */
    val weeklyWorkHours: Flow<Int> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { prefs ->
        prefs[WEEKLY_WORK_HOURS] ?: 0
    }

    /** SEÇİLEN PARA BİRİMİ */
    val selectedCurrency: Flow<Currency> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { prefs ->
        val savedCode = prefs[CURRENCY] ?: Currency.TRY.name
        Currency.entries.find { it.name == savedCode } ?: Currency.TRY
    }

    /** BÜTÇE */
    suspend fun setBudgetMoney(money: Money) {
        val json = Json.encodeToString(money)
        dataStore.edit { prefs ->
            prefs[BUDGET_AMOUNT] = json
        }
    }

    val desireBudget: Flow<Money?> = dataStore.data.map { prefs ->
        prefs[BUDGET_AMOUNT]?.let { json ->
            try {
                Json.decodeFromString<Money>(json)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun setWeeklyWorkHours(hours: Int) {
        dataStore.edit { prefs ->
            prefs[WEEKLY_WORK_HOURS] = hours
        }
    }

    suspend fun setSelectedCurrency(currency: Currency) {
        dataStore.edit { prefs ->
            prefs[CURRENCY] = currency.name
        }
    }

    suspend fun setSpendingLimit(money: Money) {
        val jsonString = Json.encodeToString(money)
        dataStore.edit { prefs ->
            prefs[SPENDING_LIMIT] = jsonString
        }
    }

    suspend fun setSavingGoal(money: Money) {
        val jsonString = Json.encodeToString(money)
        dataStore.edit { prefs ->
            prefs[SAVING_GOAL] = jsonString
        }
    }

    val spendingLimit: Flow<Money?> = dataStore.data.map { prefs ->
        prefs[SPENDING_LIMIT]?.let { jsonString ->
            try {
                Json.decodeFromString<Money>(jsonString)
            } catch (e: Exception) {
                null
            }
        }
    }

    val savingGoal: Flow<Money?> = dataStore.data.map { prefs ->
        prefs[SAVING_GOAL]?.let { jsonString ->
            try {
                Json.decodeFromString<Money>(jsonString)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun setUserName(name: String) {
        dataStore.edit { prefs ->
            prefs[USER_NAME] = name
        }
    }

    val userName: Flow<String?> = dataStore.data.map { prefs ->
        prefs[USER_NAME]
    }

    val isCategoryInitialized: Flow<Boolean> = dataStore.data.catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }.map { prefs ->
            prefs[IS_CATEGORY_INITIALIZED] ?: false
        }

    suspend fun setCategoryInitialized(initialized: Boolean) {
        dataStore.edit { prefs ->
            prefs[IS_CATEGORY_INITIALIZED] = initialized
        }
    }

    val isSubscriptionCategoryInitialized: Flow<Boolean> = dataStore.data.catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }.map { prefs ->
            prefs[IS_SUBSCRIPTION_CATEGORY_INITIALIZED] ?: false
        }

    suspend fun setSubscriptionCategoryInitialized(initialized: Boolean) {
        dataStore.edit { prefs ->
            prefs[IS_SUBSCRIPTION_CATEGORY_INITIALIZED] = initialized
        }
    }

    suspend fun setCachedCurrencyRates(cachedRatesJson: String, timestampMillis: Long) {
        dataStore.edit { prefs ->
            prefs[CURRENCY_RATES_JSON] = cachedRatesJson
            prefs[CURRENCY_RATES_TIMESTAMP] = timestampMillis.toString()
        }
    }

    suspend fun getCachedCurrencyRates(): CachedCurrencyRates? {
        val prefs = dataStore.data.first()
        val json = prefs[CURRENCY_RATES_JSON] ?: return null
        val tsString = prefs[CURRENCY_RATES_TIMESTAMP] ?: return null
        val ts = tsString.toLongOrNull() ?: return null
        return CachedCurrencyRates(json, ts)
    }

}

data class CachedCurrencyRates(
    val json: String, val timestampMillis: Long
)