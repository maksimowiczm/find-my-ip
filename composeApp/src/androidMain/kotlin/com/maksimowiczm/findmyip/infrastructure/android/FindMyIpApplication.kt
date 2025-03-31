package com.maksimowiczm.findmyip.infrastructure.android

import android.app.Application
import com.maksimowiczm.findmyip.infrastructure.di.startKoin
import org.koin.android.ext.koin.androidContext

class FindMyIpApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@FindMyIpApplication)
        }
    }
}
