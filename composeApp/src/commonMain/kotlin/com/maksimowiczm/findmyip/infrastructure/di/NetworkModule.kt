package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.BuildConfig
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.network.NetworkAddressDataSource
import com.maksimowiczm.findmyip.network.NetworkAddressDataSourceImpl
import org.koin.core.qualifier.qualifier
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.dsl.onClose

val networkModule = module {
    single(qualifier(InternetProtocolVersion.IPv4)) {
        NetworkAddressDataSourceImpl(
            providerURL = BuildConfig.IPV4_PROVIDER
        )
    }.onClose {
        // Does it makes sense to add onClose to singleton?
        it?.dispose()
    }.bind<NetworkAddressDataSource>()

    single(qualifier(InternetProtocolVersion.IPv6)) {
        NetworkAddressDataSourceImpl(
            providerURL = BuildConfig.IPV6_PROVIDER
        )
    }.onClose {
        // Does it makes sense to add onClose to singleton?
        it?.dispose()
    }.bind<NetworkAddressDataSource>()
}
