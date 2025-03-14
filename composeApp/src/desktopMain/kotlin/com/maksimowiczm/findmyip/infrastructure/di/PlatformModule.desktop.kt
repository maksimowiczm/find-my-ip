package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.data.IsMigratedFrom1
import com.maksimowiczm.findmyip.data.StringFormatRepository
import com.maksimowiczm.findmyip.infrastructure.desktop.preferencesDirectory
import com.maksimowiczm.findmyip.network.ConnectivityObserver
import java.io.File
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

actual val platformModule: Module = module {
    factory { IsMigratedFrom1 { false } }

    single {
        createDataStore {
            File(preferencesDirectory, DATASTORE_FILE_NAME).absolutePath
        }
    }

    factoryOf(::ConnectivityObserver)
    factoryOf(::StringFormatRepository)
}
