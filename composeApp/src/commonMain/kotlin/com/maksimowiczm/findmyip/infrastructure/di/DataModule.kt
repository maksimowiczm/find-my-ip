package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.data.AddressRepository
import com.maksimowiczm.findmyip.data.AddressRepositoryImpl
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    factory {
        AddressRepositoryImpl(
            ipv4DataSource = get(named(InternetProtocolVersion.IPv4)),
            ipv6DataSource = get(named(InternetProtocolVersion.IPv6))
        )
    }.bind<AddressRepository>()
}
