package com.maksimowiczm.findmyip.data.repository

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun <T> get(key: Preferences.Key<T>): Flow<T?>
    suspend fun <T> set(key: Preferences.Key<T>, value: T)
}
