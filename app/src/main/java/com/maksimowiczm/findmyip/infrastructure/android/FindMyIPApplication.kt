package com.maksimowiczm.findmyip.infrastructure.android

import android.app.Application
import com.maksimowiczm.findmyip.infrastructure.di.initKoin
import org.koin.android.ext.koin.androidContext

class FindMyIPApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@FindMyIPApplication)
        }
    }
}
