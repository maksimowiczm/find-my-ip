package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.data.network.AndroidConnectivityObserver
import com.maksimowiczm.findmyip.data.network.ConnectivityObserver
import com.maksimowiczm.findmyip.domain.preferences.NotificationPreferences
import com.maksimowiczm.findmyip.domain.preferences.NotificationPreferencesImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    factoryOf(::AndroidConnectivityObserver).bind<ConnectivityObserver>()
    factoryOf(::NotificationPreferencesImpl).bind<NotificationPreferences>()
}
