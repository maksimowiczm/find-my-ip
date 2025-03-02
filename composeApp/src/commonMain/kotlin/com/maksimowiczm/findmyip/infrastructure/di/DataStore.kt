package com.maksimowiczm.findmyip.infrastructure.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

fun createDataStore(produceFile: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { produceFile().toPath() }
    )

const val DATASTORE_FILE_NAME = "settings.preferences_pb"
