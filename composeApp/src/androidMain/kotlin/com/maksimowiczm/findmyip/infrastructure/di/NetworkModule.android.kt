package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.BuildConfig
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.network.NetworkAddressDataSource
import com.maksimowiczm.findmyip.network.NetworkAddressDataSourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

actual val networkModule = module {
    single(named(InternetProtocolVersion.IPv4)) {
        NetworkAddressDataSourceImpl(
            providerURL = BuildConfig.IPV4_PROVIDER
        )
    }.bind<NetworkAddressDataSource>()

    single(named(InternetProtocolVersion.IPv6)) {
        NetworkAddressDataSourceImpl(
            providerURL = BuildConfig.IPV6_PROVIDER
        )
    }.bind<NetworkAddressDataSource>()
}
