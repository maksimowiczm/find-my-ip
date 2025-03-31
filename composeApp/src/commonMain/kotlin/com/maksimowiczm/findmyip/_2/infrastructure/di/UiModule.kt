@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.infrastructure.di

import com.maksimowiczm.findmyip._2.ui.MigrationViewModel
import com.maksimowiczm.findmyip._2.ui.history.HistoryViewModel
import com.maksimowiczm.findmyip._2.ui.home.HomeViewModel
import com.maksimowiczm.findmyip._2.ui.settings.history.HistorySettingsViewModel
import com.maksimowiczm.findmyip._2.ui.settings.internetprotocol.InternetProtocolVersionSettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::InternetProtocolVersionSettingsViewModel)
    viewModelOf(::HistoryViewModel)
    viewModelOf(::HistorySettingsViewModel)
    viewModelOf(::MigrationViewModel)
}
