package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.application.infrastructure.date.DateProvider
import com.maksimowiczm.findmyip.application.infrastructure.local.AddressHistoryLocalDataSource
import com.maksimowiczm.findmyip.application.infrastructure.local.CurrentAddressLocalDataSource
import com.maksimowiczm.findmyip.application.infrastructure.remote.IpAddressRemoteDataSource
import com.maksimowiczm.findmyip.application.infrastructure.transaction.TransactionProvider
import com.maksimowiczm.findmyip.domain.entity.InternetProtocolVersion
import com.maksimowiczm.findmyip.domain.entity.Ip4Address
import com.maksimowiczm.findmyip.domain.entity.Ip6Address
import com.maksimowiczm.findmyip.infrastructure.BuildConfig
import com.maksimowiczm.findmyip.infrastructure.date.DateProviderImpl
import com.maksimowiczm.findmyip.infrastructure.fake.FakeAddressDataSource
import com.maksimowiczm.findmyip.infrastructure.inmemory.InMemoryIpAddressDataSource
import com.maksimowiczm.findmyip.infrastructure.ipify.IpifyAddressDataSource
import com.maksimowiczm.findmyip.infrastructure.ipify.IpifyConfigImpl
import com.maksimowiczm.findmyip.infrastructure.mapper.StringToAddressMapper
import com.maksimowiczm.findmyip.infrastructure.mapper.StringToAddressMapperImpl
import com.maksimowiczm.findmyip.infrastructure.room.FindMyIpDatabase
import com.maksimowiczm.findmyip.infrastructure.room.RoomAddressHistoryDataSource
import io.ktor.client.HttpClient
import kotlin.random.Random
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import org.koin.dsl.onClose

val infrastructureModule = module {
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

    roomModule()
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

internal const val DATABASE_NAME = "findMyIpDatabase.db"

internal expect fun Scope.database(): FindMyIpDatabase

private val Scope.database: FindMyIpDatabase
    get() = get<FindMyIpDatabase>()

private fun Module.roomModule() {
    single<FindMyIpDatabase> { database() }.binds(arrayOf(TransactionProvider::class))

    factory { database.addressHistoryDao }

    factoryOf(::RoomAddressHistoryDataSource).bind<AddressHistoryLocalDataSource>()
}
