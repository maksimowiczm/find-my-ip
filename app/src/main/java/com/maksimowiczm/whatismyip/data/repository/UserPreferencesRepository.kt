package com.maksimowiczm.whatismyip.data.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserPreferencesRepository(
    context: Context,
    private val ioDispatcher: CoroutineDispatcher
) {
    val Context.dataStore by preferencesDataStore(
        name = "settings"
    )

    private val dataStore = context.dataStore

    fun <T> get(key: Preferences.Key<T>): Flow<T?> {
        return dataStore.data.map { it[key] }
    }

    suspend fun <T> set(key: Preferences.Key<T>, value: T) {
        withContext(ioDispatcher) {
            dataStore.edit {
                it[key] = value
            }
        }
    }
}
