package com.maksimowiczm.findmyip.presentation.di

import com.maksimowiczm.findmyip.domain.entity.InternetProtocolVersion
import com.maksimowiczm.findmyip.presentation.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val presentationModule = module {
    viewModel {
        HomeViewModel(
            get(),
            get(named(InternetProtocolVersion.IPV4)),
            get(named(InternetProtocolVersion.IPV6)),
            get(named(InternetProtocolVersion.IPV4)),
            get(named(InternetProtocolVersion.IPV6)),
        )
    }
}
