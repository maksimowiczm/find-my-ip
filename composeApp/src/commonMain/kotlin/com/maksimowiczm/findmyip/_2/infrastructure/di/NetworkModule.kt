@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.infrastructure.di

import com.maksimowiczm.findmyip.BuildConfig
import com.maksimowiczm.findmyip._2.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip._2.network.NetworkAddressDataSource
import com.maksimowiczm.findmyip._2.network.NetworkAddressDataSourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module {
    single(named(InternetProtocolVersion.IPv4)) {
        NetworkAddressDataSourceImpl(
            providerURL = BuildConfig.IPV4_PROVIDER,
            connectivityObserver = get()
        )
    }.bind<NetworkAddressDataSource>()

    single(named(InternetProtocolVersion.IPv6)) {
        NetworkAddressDataSourceImpl(
            providerURL = BuildConfig.IPV6_PROVIDER,
            connectivityObserver = get()
        )
    }.bind<NetworkAddressDataSource>()
}
