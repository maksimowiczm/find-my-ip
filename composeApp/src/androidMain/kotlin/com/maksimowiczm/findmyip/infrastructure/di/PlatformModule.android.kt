package com.maksimowiczm.findmyip.infrastructure.di

import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager
import com.maksimowiczm.findmyip.data.NotificationHelper
import com.maksimowiczm.findmyip.data.StringFormatRepository
import com.maksimowiczm.findmyip.data.SystemInfoRepository
import com.maksimowiczm.findmyip.data.WorkerManager
import com.maksimowiczm.findmyip.feature.settings.history.AndroidHistorySettingsViewModel
import com.maksimowiczm.findmyip.feature.settings.language.LanguageViewModel
import com.maksimowiczm.findmyip.network.ConnectivityObserver
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

actual val platformModule = module {
    factoryOf(::SystemInfoRepository)
    factoryOf(::StringFormatRepository)
    viewModelOf(::LanguageViewModel)
    singleOf(::ConnectivityObserver)
    viewModelOf(::AndroidHistorySettingsViewModel)

    factory {
        NotificationHelper(
            context = androidApplication(),
            notificationManager = NotificationManagerCompat.from(androidApplication())
        )
    }

    factory {
        WorkerManager(
            workManager = WorkManager.getInstance(androidApplication())
        )
    }
}
