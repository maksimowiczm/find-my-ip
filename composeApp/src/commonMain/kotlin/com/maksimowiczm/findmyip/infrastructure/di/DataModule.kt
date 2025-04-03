package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.data.AddressRepository
import com.maksimowiczm.findmyip.data.AddressRepositoryImpl
import com.maksimowiczm.findmyip.data.HistoryRepositoryImpl
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.domain.ObserveHistoryUseCase
import com.maksimowiczm.findmyip.domain.ShouldShowHistoryUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.qualifier
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

val dataModule = module {
    single(qualifier("ioScope")) {
        CoroutineScope(Dispatchers.IO + SupervisorJob())
    }

    factory {
        AddressRepositoryImpl(
            ipv4source = get(qualifier(InternetProtocolVersion.IPv4)),
            ipv6source = get(qualifier(InternetProtocolVersion.IPv6)),
            dataStore = get(),
            database = get(),
            ioApplicationScope = get(qualifier("ioScope"))
        )
    }.bind<AddressRepository>()

    factory {
        HistoryRepositoryImpl(
            database = get(),
            dataStore = get()
        )
    }.binds(
        arrayOf(
            ObserveHistoryUseCase::class,
            ShouldShowHistoryUseCase::class
        )
    )
}
