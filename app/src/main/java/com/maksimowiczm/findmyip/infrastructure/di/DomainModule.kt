package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.usecase.ObserveCurrentAddressUseCase
import com.maksimowiczm.findmyip.domain.usecase.ObserveCurrentAddressUseCaseImpl
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {

    factory(named(InternetProtocol.IPv4)) {
        ObserveCurrentAddressUseCaseImpl(
            addressObserver = get(named(InternetProtocol.IPv4)),
            addressLocalDataSource = get()
        )
    }.bind<ObserveCurrentAddressUseCase>()

    factory(named(InternetProtocol.IPv6)) {
        ObserveCurrentAddressUseCaseImpl(
            addressObserver = get(named(InternetProtocol.IPv6)),
            addressLocalDataSource = get()
        )
    }.bind<ObserveCurrentAddressUseCase>()
}
