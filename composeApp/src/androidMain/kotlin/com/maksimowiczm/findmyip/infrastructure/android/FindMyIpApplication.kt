package com.maksimowiczm.findmyip.infrastructure.android

import android.app.Application
import com.maksimowiczm.findmyip.old.infrastructure.di.createDatastore
import com.maksimowiczm.findmyip.old.infrastructure.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class FindMyIpApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@FindMyIpApplication.applicationContext)

            modules(
                module {
                    single { createDatastore(androidContext()) }
                }
            )
        }
    }
}
