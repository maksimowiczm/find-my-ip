@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.infrastructure.di

import com.maksimowiczm.findmyip._2.data.IsMigratedFrom1
import com.maksimowiczm.findmyip._2.infrastructure.desktop.preferencesDirectory
import com.maksimowiczm.findmyip._2.network.ConnectivityObserver
import com.maksimowiczm.findmyip.data.StringFormatRepository
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
