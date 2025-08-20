package com.maksimowiczm.findmyip.application.di

import com.maksimowiczm.findmyip.application.usecase.ObserveAddressHistoryUseCase
import com.maksimowiczm.findmyip.application.usecase.ObserveAddressHistoryUseCaseImpl
import com.maksimowiczm.findmyip.application.usecase.ObserveCurrentIp4AddressUseCase
import com.maksimowiczm.findmyip.application.usecase.ObserveCurrentIp4AddressUseCaseImpl
import com.maksimowiczm.findmyip.application.usecase.ObserveCurrentIp6AddressUseCase
import com.maksimowiczm.findmyip.application.usecase.ObserveCurrentIp6AddressUseCaseImpl
import com.maksimowiczm.findmyip.application.usecase.RefreshIp4AddressUseCase
import com.maksimowiczm.findmyip.application.usecase.RefreshIp4AddressUseCaseImpl
import com.maksimowiczm.findmyip.application.usecase.RefreshIp6AddressUseCase
import com.maksimowiczm.findmyip.application.usecase.RefreshIp6AddressUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val applicationModule = module {
    factoryOf(::RefreshIp4AddressUseCaseImpl).bind<RefreshIp4AddressUseCase>()

    factoryOf(::RefreshIp6AddressUseCaseImpl).bind<RefreshIp6AddressUseCase>()

    factoryOf(::ObserveAddressHistoryUseCaseImpl).bind<ObserveAddressHistoryUseCase>()

    factoryOf(::ObserveCurrentIp4AddressUseCaseImpl).bind<ObserveCurrentIp4AddressUseCase>()

    factoryOf(::ObserveCurrentIp6AddressUseCaseImpl).bind<ObserveCurrentIp6AddressUseCase>()
}
