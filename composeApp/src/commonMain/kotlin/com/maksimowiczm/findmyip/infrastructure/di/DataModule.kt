package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.data.AddressRepository
import com.maksimowiczm.findmyip.data.HistoryRepository
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.domain.ClearHistoryUseCase
import com.maksimowiczm.findmyip.domain.ObserveAddressUseCase
import com.maksimowiczm.findmyip.domain.ObserveHistoryUseCase
import com.maksimowiczm.findmyip.domain.RefreshAddressesUseCase
import com.maksimowiczm.findmyip.domain.RefreshAndGetIfLatestUseCase
import com.maksimowiczm.findmyip.domain.ShouldShowHistoryUseCase
import com.maksimowiczm.findmyip.domain.TestInternetProtocolsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.qualifier
import org.koin.dsl.binds
import org.koin.dsl.module

val dataModule = module {
    single(qualifier("ioScope")) {
        CoroutineScope(Dispatchers.IO + SupervisorJob())
    }

    factory {
        AddressRepository(
            ipv4source = get(qualifier(InternetProtocolVersion.IPv4)),
            ipv6source = get(qualifier(InternetProtocolVersion.IPv6)),
            dataStore = get(),
            database = get(),
            ioApplicationScope = get(qualifier("ioScope"))
        )
    }.binds(
        arrayOf(
            ObserveAddressUseCase::class,
            RefreshAddressesUseCase::class,
            RefreshAndGetIfLatestUseCase::class,
            TestInternetProtocolsUseCase::class,
            ClearHistoryUseCase::class
        )
    )

    factory {
        HistoryRepository(
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
