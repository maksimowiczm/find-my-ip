package com.maksimowiczm.findmyip.presentation.di

import com.maksimowiczm.findmyip.presentation.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module { viewModelOf(::HomeViewModel) }
