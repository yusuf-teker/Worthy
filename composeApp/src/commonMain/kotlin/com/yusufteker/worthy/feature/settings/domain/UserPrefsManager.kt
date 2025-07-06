package com.yusufteker.worthy.feature.settings.domain

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch
import kotlinx.serialization.json.Json
import kotlinx.serialization.SerializationException

class UserPrefsManager(private val dataStore: DataStore<Preferences>) {

    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private companion object {
        val INCOME_ITEMS = stringPreferencesKey("income_items")
        val EXPENSE_ITEMS = stringPreferencesKey("expense_items")
        val BUDGET_AMOUNT = floatPreferencesKey("budget_amount")
    }

    /* ******** Flows ******** */
    /** GELİR */
    val incomeItems: Flow<List<IncomeItem>> = dataStore.data
        .catch { exception ->
            // IOException durumunda emptyPreferences() emit et
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { prefs ->
            try {
                prefs[INCOME_ITEMS]
                    ?.let { json.decodeFromString<List<IncomeItem>>(it) }
                    ?: emptyList()
            } catch (e: SerializationException) {
                // Corrupt data durumunda boş liste döner
                emptyList()
            }
        }

    /** GİDER */
    val expenseItems: Flow<List<ExpenseItem>> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { prefs ->
            try {
                prefs[EXPENSE_ITEMS]
                    ?.let { json.decodeFromString<List<ExpenseItem>>(it) }
                    ?: emptyList()
            } catch (e: SerializationException) {
                emptyList()
            }
        }

    /** BÜTÇE */
    val budgetAmount: Flow<Float> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { prefs ->
            prefs[BUDGET_AMOUNT] ?: 0f
        }

    /* ******** Kaydetme / Güncelleme ******** */

    suspend fun setIncomeItems(items: List<IncomeItem>) {
        dataStore.edit { prefs ->
            prefs[INCOME_ITEMS] = json.encodeToString(items)
        }
    }

    suspend fun setExpenseItems(items: List<ExpenseItem>) {
        dataStore.edit { prefs ->
            prefs[EXPENSE_ITEMS] = json.encodeToString(items)
        }
    }

    suspend fun setBudgetAmount(amount: Float) {
        dataStore.edit { prefs ->
            prefs[BUDGET_AMOUNT] = amount
        }
    }


    suspend fun addIncome(item: IncomeItem) {
        mutateIncomeList { list -> list + item }
    }

    suspend fun addExpense(item: ExpenseItem) {
        mutateExpenseList { list -> list + item }
    }

    suspend fun updateIncome(item: IncomeItem) {
        mutateIncomeList { list ->
            list.map { if (it.id == item.id) item else it }
        }
    }

    suspend fun updateExpense(item: ExpenseItem) {
        mutateExpenseList { list ->
            list.map { if (it.id == item.id) item else it }
        }
    }

    suspend fun removeIncome(id: String) {
        mutateIncomeList { list -> list.filterNot { it.id == id } }
    }

    suspend fun removeExpense(id: String) {
        mutateExpenseList { list -> list.filterNot { it.id == id } }
    }

    /* ******** Yardımcı Fonksiyonlar ******** */
    private suspend fun mutateIncomeList(mutator: (List<IncomeItem>) -> List<IncomeItem>) {
        dataStore.edit { prefs ->
            val current = try {
                prefs[INCOME_ITEMS]?.let { json.decodeFromString<List<IncomeItem>>(it) } ?: emptyList()
            } catch (e: SerializationException) {
                emptyList()
            }
            val updated = mutator(current)
            prefs[INCOME_ITEMS] = json.encodeToString(updated)
        }
    }

    private suspend fun mutateExpenseList(mutator: (List<ExpenseItem>) -> List<ExpenseItem>) {
        dataStore.edit { prefs ->
            val current = try {
                prefs[EXPENSE_ITEMS]?.let { json.decodeFromString<List<ExpenseItem>>(it) } ?: emptyList()
            } catch (e: SerializationException) {
                emptyList()
            }
            val updated = mutator(current)
            prefs[EXPENSE_ITEMS] = json.encodeToString(updated)
        }
    }

    /* ******** Utility ******** */
    suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }

    suspend fun exportData(): String {
        var result = ""
        dataStore.data.map { prefs ->
            json.encodeToString(prefs.asMap())
        }.collect { result = it }
        return result
    }

    val totalIncome: Flow<Float> = incomeItems.map { items ->
        items.sumOf { it.amount.toDouble() }.toFloat()
    }

    val totalExpenses: Flow<Float> = expenseItems.map { items ->
        items.sumOf { it.amount.toDouble() }.toFloat()
    }
}