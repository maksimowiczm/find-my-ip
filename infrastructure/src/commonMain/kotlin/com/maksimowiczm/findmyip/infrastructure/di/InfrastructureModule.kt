package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.application.infrastructure.AddressHistoryLocalDataSource
import com.maksimowiczm.findmyip.application.infrastructure.DateProvider
import com.maksimowiczm.findmyip.application.infrastructure.Ip4AddressRemoteDataSource
import com.maksimowiczm.findmyip.application.infrastructure.Ip6AddressRemoteDataSource
import com.maksimowiczm.findmyip.infrastructure.BuildConfig
import com.maksimowiczm.findmyip.infrastructure.date.DateProviderImpl
import com.maksimowiczm.findmyip.infrastructure.fake.FakeAddressDataSource
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
    singleOf(::DateProviderImpl).bind<DateProvider>()
    factory { StringToAddressMapperImpl }.bind<StringToAddressMapper>()

    if (BuildConfig.USE_FAKE) {
        fakeModule()
    } else {
        ipifyModule()
    }

    roomModule()
}

private fun Module.ipifyModule() {
    single(named("ipifyClient")) { HttpClient {} }.onClose { it?.close() }
    factory {
            IpifyAddressDataSource(
                config = IpifyConfigImpl,
                httpClient = get(named("ipifyClient")),
                stringToAddressMapper = get(),
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

internal const val DATABASE_NAME = "findMyIpDatabase.db"

internal expect fun Scope.database(): FindMyIpDatabase

private val Scope.database: FindMyIpDatabase
    get() = get<FindMyIpDatabase>()

private fun Module.roomModule() {
    single<FindMyIpDatabase> { database() }

    factory { database.addressHistoryDao }

    factoryOf(::RoomAddressHistoryDataSource).bind<AddressHistoryLocalDataSource>()
}
