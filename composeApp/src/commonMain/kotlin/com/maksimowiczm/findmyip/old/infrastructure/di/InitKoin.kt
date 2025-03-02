package com.maksimowiczm.findmyip.old.infrastructure.di

import com.maksimowiczm.findmyip.infrastructure.di.databaseModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) = startKoin {
    config?.invoke(this)
    modules(
        databaseModule,
        networkModule,
        dataModule,
        domainModule,
        uiModule
    )
}
