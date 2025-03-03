package com.maksimowiczm.findmyip.infrastructure.android

import android.app.Application
import com.maksimowiczm.findmyip.infrastructure.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent

class FindMyIpApplication :
    Application(),
    KoinComponent {
    override fun onCreate() {
        super.onCreate()

        val context = this.applicationContext

        initKoin {
            androidContext(context)
            workManagerFactory()
        }
    }
}
