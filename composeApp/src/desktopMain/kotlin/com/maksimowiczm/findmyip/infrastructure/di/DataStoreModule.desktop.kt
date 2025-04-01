package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.infrastructure.desktop.preferencesDirectory
import java.io.File
import org.koin.dsl.module

actual val dataStoreModule = module {
    single {
        createDataStore {
            File(preferencesDirectory, DATASTORE_FILE_NAME).absolutePath
        }
    }
}
