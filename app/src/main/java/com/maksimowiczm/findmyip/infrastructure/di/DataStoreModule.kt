package com.maksimowiczm.findmyip.infrastructure.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path
import okio.Path.Companion.toPath
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

const val DATASTORE_FILE_NAME = "user_preferences.preferences_pb"

val dataStoreModule = module {
    single {
        createDataStore {
            androidContext().filesDir.resolve(DATASTORE_FILE_NAME).absolutePath.toPath()
        }
    }
}

fun createDataStore(productFile: () -> Path): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = productFile
    )
