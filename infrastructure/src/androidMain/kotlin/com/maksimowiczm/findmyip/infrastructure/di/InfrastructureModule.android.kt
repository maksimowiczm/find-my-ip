package com.maksimowiczm.findmyip.infrastructure.di

import androidx.room.Room
import com.maksimowiczm.findmyip.application.infrastructure.background.PeriodicWorkManager
import com.maksimowiczm.findmyip.application.infrastructure.background.WorkManagerPeriodicWorkManager
import com.maksimowiczm.findmyip.application.infrastructure.dns.AndroidSystemDnsService
import com.maksimowiczm.findmyip.application.infrastructure.dns.DnsService
import com.maksimowiczm.findmyip.application.infrastructure.network.AndroidNetworkTypeObserver
import com.maksimowiczm.findmyip.application.infrastructure.network.NetworkTypeObserver
import com.maksimowiczm.findmyip.infrastructure.room.FindMyIpDatabase
import com.maksimowiczm.findmyip.infrastructure.room.FindMyIpDatabase.Companion.buildDatabase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.scope.Scope
import org.koin.dsl.bind

internal actual fun Scope.database(): FindMyIpDatabase =
    Room.databaseBuilder(
            context = get(),
            klass = FindMyIpDatabase::class.java,
            name = DATABASE_NAME,
        )
        .buildDatabase()

internal actual fun Module.platformModule() {
    factoryOf(::AndroidSystemDnsService).bind<DnsService>()
    factoryOf(::AndroidNetworkTypeObserver).bind<NetworkTypeObserver>()
    factoryOf(::WorkManagerPeriodicWorkManager).bind<PeriodicWorkManager>()
}
