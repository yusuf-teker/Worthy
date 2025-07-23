package com.yusufteker.worthy.screen.settings.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

fun createDataStore(context: Context): DataStore<Preferences> {
    return createDataStore(
        producePath = {
            context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
        }
    )
}