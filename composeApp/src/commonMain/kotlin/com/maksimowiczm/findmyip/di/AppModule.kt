package com.maksimowiczm.findmyip.di

import com.maksimowiczm.findmyip.application.infrastructure.config.AppConfig
import com.maksimowiczm.findmyip.infrastructure.FindMyIpConfig
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module { factoryOf(::FindMyIpConfig).bind<AppConfig>() }
