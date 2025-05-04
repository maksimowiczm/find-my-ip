package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.ui.page.settings.backgroundservices.BackgroundServicesPageViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val flavourModule = module {
    viewModelOf(::BackgroundServicesPageViewModel)
}
