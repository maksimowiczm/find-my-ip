package com.maksimowiczm.findmyip.old.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>,
    private val ioDispatcher: CoroutineDispatcher
) {
    /**
     * Returns the value of the preference stored at the given [key].
     * @return [Flow] emitting the current value of the preference.
     * If the preference is not set, has no default value, or is explicitly set to `null`, it will emit `null`.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> observe(key: Preferences.Key<T>): Flow<T?> =
        dataStore.data.map { it[key] ?: Keys.defaultPreferences[key] as T? }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: Preferences.Key<T>): T? = runBlocking(ioDispatcher) {
        dataStore.data.map { it[key] ?: Keys.defaultPreferences[key] as T? }.first()
    }

    suspend fun <T> set(key: Preferences.Key<T>, value: T) {
        withContext(ioDispatcher) {
            dataStore.edit {
                it[key] = value
            }
        }
    }

    /**
     * Sets atomically the given [args] in the preferences.
     */
    suspend fun set(vararg args: Preferences.Pair<*>) {
        withContext(ioDispatcher) {
            dataStore.edit {
                it.putAll(*args)
            }
        }
    }
}
