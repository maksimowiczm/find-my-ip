package com.maksimowiczm.findmyip.infrastructure.di

import androidx.work.WorkManager
import com.maksimowiczm.findmyip.domain.backgroundservices.PeriodicWorker
import com.maksimowiczm.findmyip.domain.notification.AddressNotificationService
import com.maksimowiczm.findmyip.infrastructure.android.AndroidNotificationService
import com.maksimowiczm.findmyip.infrastructure.android.AndroidPeriodicWorker
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

val androidModule = module {
    factory {
        AndroidNotificationService(get())
    }.binds(
        arrayOf(
            AddressNotificationService::class
        )
    )

    factory {
        AndroidPeriodicWorker(
            workManager = WorkManager.getInstance(get())
        )
    }.bind<PeriodicWorker>()
}
