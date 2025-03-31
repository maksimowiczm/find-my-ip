@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.infrastructure.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) = startKoin {
    config?.invoke(this)
    modules(
        databaseModule,
        networkModule,
        dataModule,
        uiModule,
        platformModule
    )
}
