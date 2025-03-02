package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.domain.DecideSaveAddressInHistoryUseCase
import com.maksimowiczm.findmyip.domain.FormatDateUseCase
import com.maksimowiczm.findmyip.domain.ObserveAddressHistoryUseCase
import com.maksimowiczm.findmyip.domain.ObserveCurrentAddressUseCase
import com.maksimowiczm.findmyip.domain.TestInternetProtocolsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::DecideSaveAddressInHistoryUseCase)
    factoryOf(::FormatDateUseCase)
    factoryOf(::ObserveAddressHistoryUseCase)
    factoryOf(::ObserveCurrentAddressUseCase)
    factoryOf(::TestInternetProtocolsUseCase)
}
