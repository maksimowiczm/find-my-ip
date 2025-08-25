package com.maksimowiczm.findmyip.di

import com.maksimowiczm.findmyip.shared.core.application.di.sharedCoreApplicationModule
import com.maksimowiczm.findmyip.shared.core.infrastructure.di.sharedCoreInfrastructureModule
import com.maksimowiczm.findmyip.shared.feature.home.di.sharedFeatureHomeModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

internal fun initKoin(config: KoinAppDeclaration? = null) = startKoin {
    config?.invoke(this)

    modules(
        appModule,
        sharedCoreApplicationModule,
        sharedCoreInfrastructureModule,
        sharedFeatureHomeModule,
    )
}
