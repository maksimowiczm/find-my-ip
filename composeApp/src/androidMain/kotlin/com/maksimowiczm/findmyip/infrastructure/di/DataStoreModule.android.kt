package com.maksimowiczm.findmyip.infrastructure.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun createDatastore(context: Context): DataStore<Preferences> = createDataStore {
    context.filesDir.resolve(DATASTORE_FILE_NAME).absolutePath
}

actual val dataStoreModule = module {
    single {
        createDatastore(androidContext())
    }
}
