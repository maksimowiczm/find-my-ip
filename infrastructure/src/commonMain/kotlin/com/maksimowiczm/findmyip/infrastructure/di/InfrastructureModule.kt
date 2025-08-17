package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.application.infrastructure.Ip4AddressLocalDataSource
import com.maksimowiczm.findmyip.application.infrastructure.Ip4AddressRemoteDataSource
import com.maksimowiczm.findmyip.infrastructure.inmemory.InMemoryIpAddressDataSource
import com.maksimowiczm.findmyip.infrastructure.ipify.IpifyAddressDataSource
import com.maksimowiczm.findmyip.infrastructure.ipify.IpifyConfigImpl
import com.maksimowiczm.findmyip.infrastructure.mapper.StringToAddressMapperImpl
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.dsl.onClose

val infrastructureModule = module {
    singleOf(::InMemoryIpAddressDataSource).bind<Ip4AddressLocalDataSource>()
    ipifyModule()
}

private fun Module.ipifyModule() {
    single(named("ipifyClient")) { HttpClient {} }.onClose { it?.close() }
    factory {
            IpifyAddressDataSource(
                config = IpifyConfigImpl,
                httpClient = get(named("ipifyClient")),
                stringToAddressMapper = StringToAddressMapperImpl,
            )
        }
        .bind<Ip4AddressRemoteDataSource>()
}
