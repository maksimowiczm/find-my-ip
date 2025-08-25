package com.maksimowiczm.findmyip.feature.background.di

import com.maksimowiczm.findmyip.feature.background.presentation.BackgroundWorkViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val backgroundModule = module { viewModelOf(::BackgroundWorkViewModel) }
