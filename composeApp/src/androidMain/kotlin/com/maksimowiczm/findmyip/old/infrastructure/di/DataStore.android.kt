package com.maksimowiczm.findmyip.old.infrastructure.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.maksimowiczm.findmyip.infrastructure.di.DATASTORE_FILE_NAME
import com.maksimowiczm.findmyip.infrastructure.di.createDataStore

fun createDatastore(context: Context): DataStore<Preferences> = createDataStore {
    context.filesDir.resolve(DATASTORE_FILE_NAME).absolutePath
}
