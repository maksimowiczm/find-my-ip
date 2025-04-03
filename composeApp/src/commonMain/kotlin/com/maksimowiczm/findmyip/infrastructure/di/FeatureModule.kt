package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.feature.currentaddress.CurrentAddressViewModel
import com.maksimowiczm.findmyip.feature.history.HistoryViewModel
import com.maksimowiczm.findmyip.feature.settings.history.HistorySettingsContainer
import com.maksimowiczm.findmyip.feature.settings.internetprotocol.InternetProtocolSettingsViewModel
import org.koin.core.module.dsl.new
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureModule = module {
    container { new(::HistorySettingsContainer) }
    viewModelOf(::CurrentAddressViewModel)
    viewModelOf(::InternetProtocolSettingsViewModel)
    viewModelOf(::HistoryViewModel)
}
