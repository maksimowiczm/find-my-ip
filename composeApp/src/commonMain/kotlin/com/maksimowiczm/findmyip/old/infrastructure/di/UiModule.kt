package com.maksimowiczm.findmyip.old.infrastructure.di

import com.maksimowiczm.findmyip.old.feature.addresshistory.AddressHistoryViewModel
import com.maksimowiczm.findmyip.old.feature.currentaddress.CurrentAddressViewModel
import com.maksimowiczm.findmyip.old.feature.settings.addresshistory.AddressHistorySettingsViewModel
import com.maksimowiczm.findmyip.old.feature.settings.addresshistory.NetworkTypeSettingsViewModel
import com.maksimowiczm.findmyip.old.feature.settings.internetprotocolversion.InternetProtocolVersionSettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::AddressHistoryViewModel)
    viewModelOf(::CurrentAddressViewModel)
    viewModelOf(::AddressHistorySettingsViewModel)
    viewModelOf(::NetworkTypeSettingsViewModel)
    viewModelOf(::InternetProtocolVersionSettingsViewModel)
}
