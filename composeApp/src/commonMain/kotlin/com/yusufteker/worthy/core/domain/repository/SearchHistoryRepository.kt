package com.yusufteker.worthy.core.domain.repository

import kotlinx.coroutines.flow.Flow


interface SearchHistoryRepository {
    val searchHistory: Flow<List<String>>
    suspend fun addSearchQuery(query: String)
    suspend fun clearHistory()
}
