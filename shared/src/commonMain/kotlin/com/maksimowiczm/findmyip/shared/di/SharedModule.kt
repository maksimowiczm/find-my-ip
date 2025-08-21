package com.maksimowiczm.findmyip.shared.di

import com.maksimowiczm.findmyip.shared.log.FindMyIpLogger
import com.maksimowiczm.findmyip.shared.log.Logger
import org.koin.dsl.bind
import org.koin.dsl.module

val sharedModule = module { factory { FindMyIpLogger }.bind<Logger>() }
