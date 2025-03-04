package com.maksimowiczm.findmyip.infrastructure.android

import android.app.Application
import android.net.ConnectivityManager
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.maksimowiczm.findmyip.data.AndroidPreferenceKeys.autoRefreshSettingsEnabledKey
import com.maksimowiczm.findmyip.infrastructure.di.initKoin
import com.maksimowiczm.findmyip.infrastructure.di.observe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

        initKoin {
            androidContext(context)
            workManagerFactory()
        }

        setupNetworkReceiver()
    }

    private fun setupNetworkReceiver() {
        val coroutineScope = CoroutineScope(Dispatchers.Main)

        val networkCallback = FindMyIpNetworkCallback(
            coroutineScope = coroutineScope,
            addressRepository = get()
        )

        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        val dataStore = get<DataStore<Preferences>>()

        coroutineScope.launch {
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

    private companion object {
        private const val TAG = "FindMyIpApplication"
    }
}
