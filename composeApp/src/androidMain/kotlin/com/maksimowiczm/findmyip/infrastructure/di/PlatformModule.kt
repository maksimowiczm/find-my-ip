package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.data.AddressRefreshWorker
import com.maksimowiczm.findmyip.data.AddressRefreshWorkerManager
import com.maksimowiczm.findmyip.data.StringFormatRepository
import com.maksimowiczm.findmyip.ui.settings.history.BackgroundWorkerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

actual val platformModule = module {
    single { createDatastore(androidContext()) }
    factory { StringFormatRepository(androidContext()) }

    viewModelOf(::BackgroundWorkerViewModel)

    single(
        createdAtStart = true
    ) {
        AddressRefreshWorkerManager(
            context = androidContext(),
            dataStore = get()
        )
    }
    workerOf(::AddressRefreshWorker)
}
