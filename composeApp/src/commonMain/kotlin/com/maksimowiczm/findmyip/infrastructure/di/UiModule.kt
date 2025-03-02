package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.ui.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::HomeViewModel)
}
