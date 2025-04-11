package com.maksimowiczm.findmyip.infrastructure.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun startKoin(config: KoinAppDeclaration? = null) = startKoin {
    config?.invoke(this)
    modules(
        networkModule,
        databaseModule,
        dataStoreModule,
        dataModule,
        platformModule,
        featureModule
    )
}
