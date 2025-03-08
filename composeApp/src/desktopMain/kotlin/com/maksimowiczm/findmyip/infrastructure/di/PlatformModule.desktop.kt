package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.data.IsMigratedFrom1
import com.maksimowiczm.findmyip.infrastructure.desktop.preferencesDirectory
import java.io.File
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module = module {
    factory { IsMigratedFrom1 { false } }.bind<IsMigratedFrom1>()

    single {
        createDataStore {
            File(preferencesDirectory, DATASTORE_FILE_NAME).absolutePath
        }
    }
}
