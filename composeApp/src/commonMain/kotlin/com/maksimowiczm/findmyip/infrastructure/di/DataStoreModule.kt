package com.maksimowiczm.findmyip.infrastructure.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath
import org.koin.core.module.Module

fun createDataStore(produceFile: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { produceFile().toPath() }
    )

const val DATASTORE_FILE_NAME = "settings.preferences_pb"

expect val dataStoreModule: Module

fun <T> DataStore<Preferences>.observe(key: Preferences.Key<T>) = data.map { preferences ->
    preferences[key]
}

suspend fun <T> DataStore<Preferences>.get(key: Preferences.Key<T>): T? = observe(key).first()

suspend fun DataStore<Preferences>.set(vararg pairs: Preferences.Pair<*>) {
    edit { preferences ->
        preferences.putAll(*pairs)
    }
}
