package com.maksimowiczm.findmyip.di

import com.maksimowiczm.findmyip.application.di.applicationModule
import com.maksimowiczm.findmyip.infrastructure.di.infrastructureModule
import com.maksimowiczm.findmyip.presentation.di.presentationModule
import com.maksimowiczm.findmyip.shared.di.sharedModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) = startKoin {
    config?.invoke(this)

    modules(appModule, applicationModule, infrastructureModule, presentationModule, sharedModule)
}
