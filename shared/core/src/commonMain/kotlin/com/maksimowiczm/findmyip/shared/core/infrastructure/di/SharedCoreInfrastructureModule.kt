package com.maksimowiczm.findmyip.shared.core.infrastructure.di

import com.maksimowiczm.findmyip.shared.BuildConfig
import com.maksimowiczm.findmyip.shared.core.application.infrastructure.date.DateProvider
import com.maksimowiczm.findmyip.shared.core.application.infrastructure.local.CurrentAddressLocalDataSource
import com.maksimowiczm.findmyip.shared.core.application.infrastructure.remote.IpAddressRemoteDataSource
import com.maksimowiczm.findmyip.shared.core.domain.InternetProtocolVersion
import com.maksimowiczm.findmyip.shared.core.domain.Ip4Address
import com.maksimowiczm.findmyip.shared.core.domain.Ip6Address
import com.maksimowiczm.findmyip.shared.core.infrastructure.date.DateProviderImpl
import com.maksimowiczm.findmyip.shared.core.infrastructure.fake.FakeAddressDataSource
import com.maksimowiczm.findmyip.shared.core.infrastructure.inmemory.InMemoryIpAddressDataSource
import com.maksimowiczm.findmyip.shared.core.infrastructure.ipify.IpifyAddressDataSource
import com.maksimowiczm.findmyip.shared.core.infrastructure.ipify.IpifyConfigImpl
import com.maksimowiczm.findmyip.shared.core.infrastructure.mapper.StringToAddressMapper
import com.maksimowiczm.findmyip.shared.core.infrastructure.mapper.StringToAddressMapperImpl
import io.ktor.client.HttpClient
import kotlin.random.Random
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.dsl.onClose

val sharedCoreInfrastructureModule = module {
    platformModule()

    singleOf(::DateProviderImpl).bind<DateProvider>()
    factory { StringToAddressMapperImpl }.bind<StringToAddressMapper>()
    single(named(InternetProtocolVersion.IPV4)) { InMemoryIpAddressDataSource<Ip4Address>() }
        .bind<CurrentAddressLocalDataSource<Ip4Address>>()
    single(named(InternetProtocolVersion.IPV6)) { InMemoryIpAddressDataSource<Ip6Address>() }
        .bind<CurrentAddressLocalDataSource<Ip6Address>>()

    if (BuildConfig.USE_FAKE) {
        fakeModule()
    } else {
        ipifyModule()
    }
}

internal expect fun Module.platformModule()

private fun Module.ipifyModule() {
    single(named("ipifyClient")) { HttpClient {} }.onClose { it?.close() }
    single {
        IpifyAddressDataSource(
            config = IpifyConfigImpl,
            httpClient = get(named("ipifyClient")),
            stringToAddressMapper = get(),
        )
    }
    factory(named(InternetProtocolVersion.IPV4)) { get<IpifyAddressDataSource>().ip4Wrapper() }
        .bind<IpAddressRemoteDataSource<Ip4Address>>()
    factory(named(InternetProtocolVersion.IPV6)) { get<IpifyAddressDataSource>().ip6Wrapper() }
        .bind<IpAddressRemoteDataSource<Ip6Address>>()
}

private fun Module.fakeModule() {
    single {
        FakeAddressDataSource(random = Random(0), stringToAddressMapper = StringToAddressMapperImpl)
    }
    factory(named(InternetProtocolVersion.IPV4)) { get<FakeAddressDataSource>().ip4Wrapper() }
        .bind<IpAddressRemoteDataSource<Ip4Address>>()
    factory(named(InternetProtocolVersion.IPV6)) { get<FakeAddressDataSource>().ip6Wrapper() }
        .bind<IpAddressRemoteDataSource<Ip6Address>>()
}
