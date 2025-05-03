package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.domain.notification.AddressNotificationService
import com.maksimowiczm.findmyip.infrastructure.android.AndroidNotificationService
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
}
