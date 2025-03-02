package com.maksimowiczm.findmyip.infrastructure.android

import android.app.Application
import com.maksimowiczm.findmyip.infrastructure.di.initKoin
import com.maksimowiczm.findmyip.old.infrastructure.di.createDatastore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class FindMyIpApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val context = this.applicationContext

        initKoin {
            androidContext(context)

            modules(
                module {
                    single { createDatastore(androidContext()) }
                }
            )
        }
    }
}
