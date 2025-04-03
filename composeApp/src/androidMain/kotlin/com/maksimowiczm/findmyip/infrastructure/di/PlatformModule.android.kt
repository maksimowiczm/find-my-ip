package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.data.StringFormatRepository
import com.maksimowiczm.findmyip.data.SystemInfoRepository
import com.maksimowiczm.findmyip.feature.settings.language.LanguageViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

actual val platformModule = module {
    factoryOf(::SystemInfoRepository)
    factoryOf(::StringFormatRepository)
    viewModelOf(::LanguageViewModel)
}
