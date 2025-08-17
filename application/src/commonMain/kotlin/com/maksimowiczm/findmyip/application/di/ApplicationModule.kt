package com.maksimowiczm.findmyip.application.di

import com.maksimowiczm.findmyip.application.usecase.ObserveCurrentIp4AddressUseCase
import com.maksimowiczm.findmyip.application.usecase.ObserveCurrentIp4AddressUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val applicationModule = module {
    factoryOf(::ObserveCurrentIp4AddressUseCaseImpl).bind<ObserveCurrentIp4AddressUseCase>()
}
