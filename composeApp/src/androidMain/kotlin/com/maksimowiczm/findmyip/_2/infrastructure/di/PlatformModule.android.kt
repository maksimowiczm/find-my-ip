@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.infrastructure.di

import com.maksimowiczm.findmyip._2.data.AddressRefreshWorker
import com.maksimowiczm.findmyip._2.data.AddressRefreshWorkerManager
import com.maksimowiczm.findmyip._2.data.IsMigratedFrom1
import com.maksimowiczm.findmyip._2.data.SystemInfoRepository
import com.maksimowiczm.findmyip._2.isMigratedFrom1
import com.maksimowiczm.findmyip._2.network.ConnectivityObserver
import com.maksimowiczm.findmyip._2.ui.settings.autorefresh.AutoRefreshSettingsViewModel
import com.maksimowiczm.findmyip._2.ui.settings.history.BackgroundWorkerViewModel
import com.maksimowiczm.findmyip._2.ui.settings.language.LanguageViewModel
import com.maksimowiczm.findmyip.data.StringFormatRepository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {
    single { createDatastore(androidContext()) }
    factory { StringFormatRepository(androidContext()) }

    viewModelOf(::BackgroundWorkerViewModel)

    factory {
        AddressRefreshWorkerManager(
            context = androidContext(),
            dataStore = get()
        )
    }
    workerOf(::AddressRefreshWorker)

    factory { androidContext().isMigratedFrom1 }.bind<IsMigratedFrom1>()

    factoryOf(::SystemInfoRepository)

    viewModelOf(::LanguageViewModel)

    viewModelOf(::AutoRefreshSettingsViewModel)

    factoryOf(::ConnectivityObserver)
}
