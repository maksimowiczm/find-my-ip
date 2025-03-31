package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.feature.currentaddress.CurrentAddressContainer
import org.koin.core.module.dsl.new
import org.koin.dsl.module

val featureModule = module {
    container { new(::CurrentAddressContainer) }
}
