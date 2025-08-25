package com.maksimowiczm.findmyip.shared.feature.home.di

import com.maksimowiczm.findmyip.shared.core.domain.InternetProtocolVersion
import com.maksimowiczm.findmyip.shared.feature.home.persentation.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val sharedFeatureHomeModule = module {
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
