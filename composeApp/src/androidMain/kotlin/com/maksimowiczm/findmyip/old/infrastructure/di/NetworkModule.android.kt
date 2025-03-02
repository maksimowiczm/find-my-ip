package com.maksimowiczm.findmyip.old.infrastructure.di

import com.maksimowiczm.findmyip.BuildConfig
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.old.network.ConnectivityObserver
import com.maksimowiczm.findmyip.old.network.CurrentAddressDataSource
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val networkModule = module {
    singleOf(::ConnectivityObserver)
    single(
        named(InternetProtocolVersion.IPv4)
    ) {
        CurrentAddressDataSource(
            networkDispatcher = Dispatchers.IO,
            connectivityObserver = get(),
            addressProviderUrl = BuildConfig.IPV4_PROVIDER,
            internetProtocolVersion = InternetProtocolVersion.IPv4
        )
    }
    single(
        named(InternetProtocolVersion.IPv6)
    ) {
        CurrentAddressDataSource(
            networkDispatcher = Dispatchers.IO,
            connectivityObserver = get(),
            addressProviderUrl = BuildConfig.IPV6_PROVIDER,
            internetProtocolVersion = InternetProtocolVersion.IPv6
        )
    }
}
