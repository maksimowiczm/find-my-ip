package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.data.PublicAddressRepository
import com.maksimowiczm.findmyip.data.PublicAddressRepositoryImpl
import com.maksimowiczm.findmyip.data.UserPreferencesRepository
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.database.FindMyIpDatabase
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    factory {
        PublicAddressRepositoryImpl(
            ipv4DataSource = get(named(InternetProtocolVersion.IPv4)),
            ipv6DataSource = get(named(InternetProtocolVersion.IPv6)),
            database = get(),
            addressEntityDao = get<FindMyIpDatabase>().addressEntityDao()
        )
    }.bind<PublicAddressRepository>()
    factory {
        UserPreferencesRepository(
            dataStore = get(),
            // TODO
            ioDispatcher = Dispatchers.IO
        )
    }
}
