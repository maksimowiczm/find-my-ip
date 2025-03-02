package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.feature.addresshistory.AddressHistoryViewModel
import com.maksimowiczm.findmyip.feature.currentaddress.CurrentAddressViewModel
import com.maksimowiczm.findmyip.feature.settings.addresshistory.AddressHistorySettingsViewModel
import com.maksimowiczm.findmyip.feature.settings.addresshistory.NetworkTypeSettingsViewModel
import com.maksimowiczm.findmyip.feature.settings.internetprotocolversion.InternetProtocolVersionSettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::AddressHistoryViewModel)
    viewModelOf(::CurrentAddressViewModel)
    viewModelOf(::AddressHistorySettingsViewModel)
    viewModelOf(::NetworkTypeSettingsViewModel)
    viewModelOf(::InternetProtocolVersionSettingsViewModel)
}
