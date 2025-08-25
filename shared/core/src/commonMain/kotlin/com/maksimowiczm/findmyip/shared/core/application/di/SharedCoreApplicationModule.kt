package com.maksimowiczm.findmyip.shared.core.application.di

import com.maksimowiczm.findmyip.shared.core.application.usecase.ObserveAddressHistoryUseCase
import com.maksimowiczm.findmyip.shared.core.application.usecase.ObserveAddressHistoryUseCaseImpl
import com.maksimowiczm.findmyip.shared.core.application.usecase.ObserveCurrentIpAddressUseCase
import com.maksimowiczm.findmyip.shared.core.application.usecase.ObserveCurrentIpAddressUseCaseImpl
import com.maksimowiczm.findmyip.shared.core.application.usecase.RefreshAddressUseCase
import com.maksimowiczm.findmyip.shared.core.application.usecase.RefreshAddressUseCaseImpl
import com.maksimowiczm.findmyip.shared.core.application.usecase.SaveAddressHistoryUseCase
import com.maksimowiczm.findmyip.shared.core.application.usecase.SaveAddressHistoryUseCaseImpl
import com.maksimowiczm.findmyip.shared.core.domain.InternetProtocolVersion
import com.maksimowiczm.findmyip.shared.core.domain.Ip4Address
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val sharedCoreApplicationModule = module {
    refreshAddressUseCase(InternetProtocolVersion.IPV4)
    refreshAddressUseCase(InternetProtocolVersion.IPV6)

    factory {
            ObserveAddressHistoryUseCaseImpl(
                get(),
                get(named(InternetProtocolVersion.IPV4)),
                get(named(InternetProtocolVersion.IPV6)),
            )
        }
        .bind<ObserveAddressHistoryUseCase>()

    observeCurrentIpAddressUseCase(InternetProtocolVersion.IPV4)
    observeCurrentIpAddressUseCase(InternetProtocolVersion.IPV6)

    factoryOf(::SaveAddressHistoryUseCaseImpl).bind<SaveAddressHistoryUseCase>()
}

private fun Module.refreshAddressUseCase(protocolVersion: InternetProtocolVersion) {
    factory(named(protocolVersion)) {
            RefreshAddressUseCaseImpl<Ip4Address>(
                get(),
                get(named(protocolVersion)),
                get(named(protocolVersion)),
                get(),
                get(),
                get(),
                get(),
            )
        }
        .bind<RefreshAddressUseCase>()
}

private fun Module.observeCurrentIpAddressUseCase(protocolVersion: InternetProtocolVersion) {
    factory(named(protocolVersion)) {
            ObserveCurrentIpAddressUseCaseImpl<Ip4Address>(get(named(protocolVersion)))
        }
        .bind<ObserveCurrentIpAddressUseCase<Ip4Address>>()
}
