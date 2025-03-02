package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.ui.home.HomeViewModel
import com.maksimowiczm.findmyip.ui.settings.internetprotocol.InternetProtocolVersionSettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::InternetProtocolVersionSettingsViewModel)
}
