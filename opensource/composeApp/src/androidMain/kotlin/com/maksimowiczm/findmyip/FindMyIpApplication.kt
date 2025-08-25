package com.maksimowiczm.findmyip

import android.app.Application
import com.maksimowiczm.findmyip.di.initKoin
import org.koin.android.ext.koin.androidContext

class FindMyIpApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin { androidContext(this@FindMyIpApplication) }
    }
}
