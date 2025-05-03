package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.repository.AddressRepository
import com.maksimowiczm.findmyip.domain.repository.AddressRepositoryImpl
import com.maksimowiczm.findmyip.domain.usecase.HandleNewAddressUseCase
import com.maksimowiczm.findmyip.domain.usecase.HandleNewAddressUseCaseImpl
import com.maksimowiczm.findmyip.domain.usecase.ObserveCurrentAddressUseCase
import com.maksimowiczm.findmyip.domain.usecase.ObserveCurrentAddressUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {
    factory {
        AddressRepositoryImpl(
            localDataSource = get()
        )
    }.bind<AddressRepository>()

    factory(named(InternetProtocol.IPv4)) {
        ObserveCurrentAddressUseCaseImpl(
            addressObserver = get(named(InternetProtocol.IPv4)),
            addressLocalDataSource = get(),
            handleNewAddressUseCase = get()
        )
    }.bind<ObserveCurrentAddressUseCase>()

    factory(named(InternetProtocol.IPv6)) {
        ObserveCurrentAddressUseCaseImpl(
            addressObserver = get(named(InternetProtocol.IPv6)),
            addressLocalDataSource = get(),
            handleNewAddressUseCase = get()
        )
    }.bind<ObserveCurrentAddressUseCase>()

    factoryOf(::HandleNewAddressUseCaseImpl).bind<HandleNewAddressUseCase>()
}
