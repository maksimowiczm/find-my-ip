package com.maksimowiczm.findmyip.infrastructure.android

import android.app.Application
import com.maksimowiczm.findmyip.data.NotificationHelper
import com.maksimowiczm.findmyip.infrastructure.di.startKoin
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory

class FindMyIpApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        NotificationHelper.initNotificationChannel(this)

        startKoin {
            androidContext(this@FindMyIpApplication)
            workManagerFactory()
        }
    }
}
