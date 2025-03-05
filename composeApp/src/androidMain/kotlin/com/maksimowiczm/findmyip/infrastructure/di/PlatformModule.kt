package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.data.AddressRefreshWorker
import com.maksimowiczm.findmyip.data.AddressRefreshWorkerManager
import com.maksimowiczm.findmyip.data.IsMigratedFrom1
import com.maksimowiczm.findmyip.data.StringFormatRepository
import com.maksimowiczm.findmyip.data.SystemInfoRepository
import com.maksimowiczm.findmyip.isMigratedFrom1
import com.maksimowiczm.findmyip.ui.settings.autorefresh.AutoRefreshSettingsViewModel
import com.maksimowiczm.findmyip.ui.settings.history.BackgroundWorkerViewModel
import com.maksimowiczm.findmyip.ui.settings.language.LanguageViewModel
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
}
