package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.application.infrastructure.DateProvider
import com.maksimowiczm.findmyip.application.infrastructure.Ip4AddressLocalDataSource
import com.maksimowiczm.findmyip.application.infrastructure.Ip4AddressRemoteDataSource
import com.maksimowiczm.findmyip.application.infrastructure.Ip6AddressLocalDataSource
import com.maksimowiczm.findmyip.application.infrastructure.Ip6AddressRemoteDataSource
import com.maksimowiczm.findmyip.infrastructure.BuildConfig
import com.maksimowiczm.findmyip.infrastructure.date.DateProviderImpl
import com.maksimowiczm.findmyip.infrastructure.fake.FakeAddressDataSource
import com.maksimowiczm.findmyip.infrastructure.inmemory.InMemoryIpAddressDataSource
import com.maksimowiczm.findmyip.infrastructure.ipify.IpifyAddressDataSource
import com.maksimowiczm.findmyip.infrastructure.ipify.IpifyConfigImpl
import com.maksimowiczm.findmyip.infrastructure.mapper.StringToAddressMapperImpl
import io.ktor.client.HttpClient
import kotlin.random.Random
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import org.koin.dsl.onClose

val infrastructureModule = module {
    singleOf(::InMemoryIpAddressDataSource)
        .binds(arrayOf(Ip4AddressLocalDataSource::class, Ip6AddressLocalDataSource::class))
    singleOf(::DateProviderImpl).bind<DateProvider>()

    if (BuildConfig.USE_FAKE) {
        fakeModule()
    } else {
        ipifyModule()
    }
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
        .binds(arrayOf(Ip4AddressRemoteDataSource::class, Ip6AddressRemoteDataSource::class))
}

private fun Module.fakeModule() {
    single {
            FakeAddressDataSource(
                random = Random(0),
                stringToAddressMapper = StringToAddressMapperImpl,
            )
        }
        .binds(arrayOf(Ip4AddressRemoteDataSource::class, Ip6AddressRemoteDataSource::class))
}
