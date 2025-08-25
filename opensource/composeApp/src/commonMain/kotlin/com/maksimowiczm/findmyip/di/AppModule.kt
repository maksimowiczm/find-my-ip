package com.maksimowiczm.findmyip.di

import com.maksimowiczm.findmyip.application.infrastructure.OpensourceAppConfig
import com.maksimowiczm.findmyip.application.usecase.ObserveSponsorshipMethodsUseCase
import com.maksimowiczm.findmyip.application.usecase.ObserveSponsorshipMethodsUseCaseImpl
import com.maksimowiczm.findmyip.feature.background.presentation.BackgroundWorkViewModel
import com.maksimowiczm.findmyip.infrastructure.FindMyIpConfig
import com.maksimowiczm.findmyip.infrastructure.room.FindMyIpDatabase
import com.maksimowiczm.findmyip.shared.core.application.infrastructure.config.AppConfig
import com.maksimowiczm.findmyip.shared.core.application.infrastructure.local.AddressHistoryLocalDataSource
import com.maksimowiczm.findmyip.shared.core.application.infrastructure.transaction.TransactionProvider
import com.maksimowiczm.findmyip.shared.core.infrastructure.room.RoomAddressHistoryDataSource
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.scope.Scope
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

internal val appModule = module {
    factoryOf(::FindMyIpConfig).binds(arrayOf(AppConfig::class, OpensourceAppConfig::class))
    factoryOf(::ObserveSponsorshipMethodsUseCaseImpl).bind<ObserveSponsorshipMethodsUseCase>()
    roomModule()

    viewModelOf(::BackgroundWorkViewModel)
}

internal const val DATABASE_NAME = "database"

internal expect fun Scope.database(): FindMyIpDatabase

private val Scope.database: FindMyIpDatabase
    get() = get<FindMyIpDatabase>()

private fun Module.roomModule() {
    single<FindMyIpDatabase> { database() }.binds(arrayOf(TransactionProvider::class))

    factory { database.addressHistoryDao }

    factoryOf(::RoomAddressHistoryDataSource).bind<AddressHistoryLocalDataSource>()
}
