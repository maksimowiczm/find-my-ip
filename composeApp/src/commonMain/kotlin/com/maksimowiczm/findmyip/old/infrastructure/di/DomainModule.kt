package com.maksimowiczm.findmyip.old.infrastructure.di

import com.maksimowiczm.findmyip.old.domain.DecideSaveAddressInHistoryUseCase
import com.maksimowiczm.findmyip.old.domain.FormatDateUseCase
import com.maksimowiczm.findmyip.old.domain.ObserveAddressHistoryUseCase
import com.maksimowiczm.findmyip.old.domain.ObserveCurrentAddressUseCase
import com.maksimowiczm.findmyip.old.domain.TestInternetProtocolsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::DecideSaveAddressInHistoryUseCase)
    factoryOf(::FormatDateUseCase)
    factoryOf(::ObserveAddressHistoryUseCase)
    factoryOf(::ObserveCurrentAddressUseCase)
    factoryOf(::TestInternetProtocolsUseCase)
}
