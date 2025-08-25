package com.maksimowiczm.findmyip.shared.core.infrastructure.di

import com.maksimowiczm.findmyip.shared.core.application.infrastructure.background.PeriodicWorkManager
import com.maksimowiczm.findmyip.shared.core.application.infrastructure.background.WorkManagerPeriodicWorkManager
import com.maksimowiczm.findmyip.shared.core.application.infrastructure.dns.AndroidSystemDnsService
import com.maksimowiczm.findmyip.shared.core.application.infrastructure.dns.DnsService
import com.maksimowiczm.findmyip.shared.core.application.infrastructure.log.FindMyIpLogger
import com.maksimowiczm.findmyip.shared.core.application.infrastructure.log.Logger
import com.maksimowiczm.findmyip.shared.core.application.infrastructure.network.AndroidNetworkTypeObserver
import com.maksimowiczm.findmyip.shared.core.application.infrastructure.network.NetworkTypeObserver
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind

internal actual fun Module.platformModule() {
    factoryOf(::AndroidSystemDnsService).bind<DnsService>()
    factoryOf(::AndroidNetworkTypeObserver).bind<NetworkTypeObserver>()
    factoryOf(::WorkManagerPeriodicWorkManager).bind<PeriodicWorkManager>()
    single { FindMyIpLogger }.bind<Logger>()
}
