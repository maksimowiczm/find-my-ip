package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.source.AddressObserver
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val addressSourceModule = module {
    single(named(InternetProtocol.IPv4)) {
        FakeAddressObserver(InternetProtocol.IPv4)
    }.bind<AddressObserver>()

    single(named(InternetProtocol.IPv6)) {
        FakeAddressObserver(InternetProtocol.IPv6)
    }.bind<AddressObserver>()
}
