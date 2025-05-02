package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.ui.page.home.HomePageViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val uiModule = module {
    viewModel {
        HomePageViewModel(
            ipv4 = get(named(InternetProtocol.IPv4)),
            ipv6 = get(named(InternetProtocol.IPv6))
        )
    }
}
