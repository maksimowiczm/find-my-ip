package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.domain.mapper.AddressMapper
import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.repository.AddressRepository
import com.maksimowiczm.findmyip.domain.repository.AddressRepositoryImpl
import com.maksimowiczm.findmyip.domain.usecase.BackgroundRefreshUseCase
import com.maksimowiczm.findmyip.domain.usecase.BackgroundRefreshUseCaseImpl
import com.maksimowiczm.findmyip.domain.usecase.HandleNewAddressUseCase
import com.maksimowiczm.findmyip.domain.usecase.HandleNewAddressUseCaseImpl
import com.maksimowiczm.findmyip.domain.usecase.InsertNetworkAddressIfChangedUseCase
import com.maksimowiczm.findmyip.domain.usecase.InsertNetworkAddressIfChangedUseCaseImpl
import com.maksimowiczm.findmyip.domain.usecase.ObserveCurrentAddressUseCase
import com.maksimowiczm.findmyip.domain.usecase.ObserveCurrentAddressUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {
    factory {
        AddressRepositoryImpl(
            localDataSource = get(),
            addressMapper = AddressMapper
        )
    }.bind<AddressRepository>()

    factory {
        InsertNetworkAddressIfChangedUseCaseImpl(
            addressLocalDataSource = get(),
            addressMapper = AddressMapper
        )
    }.bind<InsertNetworkAddressIfChangedUseCase>()

    factory(named(InternetProtocol.IPv4)) {
        ObserveCurrentAddressUseCaseImpl(
            addressObserver = get(named(InternetProtocol.IPv4)),
            insertNetworkAddressIfChangedUseCase = get(),
            handleNewAddressUseCase = get()
        )
    }.bind<ObserveCurrentAddressUseCase>()

    factory(named(InternetProtocol.IPv6)) {
        ObserveCurrentAddressUseCaseImpl(
            addressObserver = get(named(InternetProtocol.IPv6)),
            insertNetworkAddressIfChangedUseCase = get(),
            handleNewAddressUseCase = get()
        )
    }.bind<ObserveCurrentAddressUseCase>()

    factoryOf(::HandleNewAddressUseCaseImpl).bind<HandleNewAddressUseCase>()

    factory(named(InternetProtocol.IPv4)) {
        BackgroundRefreshUseCaseImpl(
            addressObserver = get(named(InternetProtocol.IPv4)),
            insertNetworkAddressIfChangedUseCase = get(),
            handleNewAddressUseCase = get()
        )
    }.bind<BackgroundRefreshUseCase>()

    factory(named(InternetProtocol.IPv6)) {
        BackgroundRefreshUseCaseImpl(
            addressObserver = get(named(InternetProtocol.IPv6)),
            insertNetworkAddressIfChangedUseCase = get(),
            handleNewAddressUseCase = get()
        )
    }.bind<BackgroundRefreshUseCase>()
}
