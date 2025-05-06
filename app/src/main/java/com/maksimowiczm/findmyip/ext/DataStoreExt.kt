package com.maksimowiczm.findmyip.ext

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

fun <T> DataStore<Preferences>.observe(key: Preferences.Key<T>): Flow<T?> =
    data.map { preferences ->
        preferences[key]
    }

suspend fun <T> DataStore<Preferences>.get(key: Preferences.Key<T>): T? = observe(key).first()

fun <T> DataStore<Preferences>.getBlocking(key: Preferences.Key<T>) = runBlocking { get(key) }

suspend fun DataStore<Preferences>.set(vararg pairs: Preferences.Pair<*>) {
    edit { preferences ->
        preferences.putAll(*pairs)
    }
}
