package com.maksimowiczm.findmyip.old.infrastructure.di

import com.maksimowiczm.findmyip.database.FindMyIpDatabase
import com.maksimowiczm.findmyip.old.data.PublicAddressRepository
import com.maksimowiczm.findmyip.old.data.PublicAddressRepositoryImpl
import com.maksimowiczm.findmyip.old.data.UserPreferencesRepository
import com.maksimowiczm.findmyip.old.data.model.InternetProtocolVersion
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
