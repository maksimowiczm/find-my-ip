package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.data.AddressRepository
import com.maksimowiczm.findmyip.data.AddressRepositoryImpl
import com.maksimowiczm.findmyip.data.initializer.AppInitializer
import com.maksimowiczm.findmyip.data.initializer.IpFeaturesInitializer
import com.maksimowiczm.findmyip.data.initializer.NetworkTypeInitializer
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.database.FindMyIpDatabase
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    factory {
        AddressRepositoryImpl(
            ipv4DataSource = get(named(InternetProtocolVersion.IPv4)),
            ipv6DataSource = get(named(InternetProtocolVersion.IPv6)),
            dataStore = get(),
            dao = get<FindMyIpDatabase>().addressEntityDao()
        )
    }.bind<AddressRepository>()

//    addDemoAddressRepository()

    factory {
        AppInitializer(
            initializers = listOf(
                NetworkTypeInitializer(get()),
                IpFeaturesInitializer(
                    dataStore = get(),
                    ipv4Source = get(named(InternetProtocolVersion.IPv4)),
                    ipv6Source = get(named(InternetProtocolVersion.IPv6))
                )
            )
        )
    }
}
