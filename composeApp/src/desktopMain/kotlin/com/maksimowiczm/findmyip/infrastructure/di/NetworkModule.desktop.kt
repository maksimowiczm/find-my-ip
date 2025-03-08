package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.network.NetworkAddressDataSource
import com.maksimowiczm.findmyip.network.NetworkAddressDataSourceImpl
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

actual val networkModule: Module = module {
    single(named(InternetProtocolVersion.IPv4)) {
        NetworkAddressDataSourceImpl(
            providerURL = "https://api.ipify.org",
            connectivityObserver = get()
        )
    }.bind<NetworkAddressDataSource>()

    single(named(InternetProtocolVersion.IPv6)) {
        NetworkAddressDataSourceImpl(
            providerURL = "https://api6.ipify.org",
            connectivityObserver = get()
        )
    }.bind<NetworkAddressDataSource>()
}
