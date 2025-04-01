package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.feature.currentaddress.CurrentAddressContainer
import com.maksimowiczm.findmyip.feature.settings.history.HistorySettingsContainer
import com.maksimowiczm.findmyip.feature.settings.internetprotocol.InternetProtocolSettingsContainer
import org.koin.core.module.dsl.new
import org.koin.dsl.module

val featureModule = module {
    container { new(::CurrentAddressContainer) }
    container { new(::InternetProtocolSettingsContainer) }
    container { new(::HistorySettingsContainer) }
}
