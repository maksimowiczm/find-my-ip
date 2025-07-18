package com.maksimowiczm.findmyip.infrastructure.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)

        modules(
            androidModule,
            databaseModule,
            dataModule,
            addressSourceModule,
            dataStoreModule,
            domainModule,
            uiModule
        )
    }
}
