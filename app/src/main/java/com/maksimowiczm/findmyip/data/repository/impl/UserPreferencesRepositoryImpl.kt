package com.maksimowiczm.findmyip.data.repository.impl

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.maksimowiczm.findmyip.data.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserPreferencesRepositoryImpl(
    context: Context,
    private val ioDispatcher: CoroutineDispatcher
) : UserPreferencesRepository {
    val Context.dataStore by preferencesDataStore(
        name = "settings"
    )

    private val dataStore = context.dataStore

    override fun <T> get(key: Preferences.Key<T>): Flow<T?> {
        return dataStore.data.map { it[key] }
    }

    override suspend fun <T> set(key: Preferences.Key<T>, value: T) {
        withContext(ioDispatcher) {
            dataStore.edit {
                it[key] = value
            }
        }
    }
}
