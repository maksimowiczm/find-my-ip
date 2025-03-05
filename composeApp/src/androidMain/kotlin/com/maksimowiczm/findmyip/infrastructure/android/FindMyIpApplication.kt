package com.maksimowiczm.findmyip.infrastructure.android

import android.app.Application
import android.net.ConnectivityManager
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.maksimowiczm.findmyip.data.AddressRefreshWorkerManager
import com.maksimowiczm.findmyip.data.AndroidPreferenceKeys.autoRefreshSettingsEnabledKey
import com.maksimowiczm.findmyip.data.initializer.AppInitializer
import com.maksimowiczm.findmyip.infrastructure.di.initKoin
import com.maksimowiczm.findmyip.infrastructure.di.observe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class FindMyIpApplication :
    Application(),
    KoinComponent {
    override fun onCreate() {
        super.onCreate()

        val context = this.applicationContext
        val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

        initKoin {
            androidContext(context)
            workManagerFactory()
        }

        coroutineScope.launch {
            get<AppInitializer>().invoke()

            setupNetworkReceiver()
            setUpWorkerManager()
        }
    }

    private fun CoroutineScope.setupNetworkReceiver() {
        val networkCallback = FindMyIpNetworkCallback(
            coroutineScope = this,
            addressRepository = get()
        )

        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        val dataStore = get<DataStore<Preferences>>()

        launch {
            dataStore
                .observe(autoRefreshSettingsEnabledKey)
                .map { it ?: false }
                .distinctUntilChanged()
                .collect { enabled ->
                    try {
                        if (enabled) {
                            connectivityManager.registerDefaultNetworkCallback(networkCallback)
                        } else {
                            connectivityManager.unregisterNetworkCallback(networkCallback)
                        }
                    } catch (e: Exception) {
                        Log.w(TAG, "Network callback failure", e)
                    }
                }
        }
    }

    private fun CoroutineScope.setUpWorkerManager() {
        get<AddressRefreshWorkerManager>().run {
            launchCancellationTask()
        }
    }

    private companion object {
        private const val TAG = "FindMyIpApplication"
    }
}
