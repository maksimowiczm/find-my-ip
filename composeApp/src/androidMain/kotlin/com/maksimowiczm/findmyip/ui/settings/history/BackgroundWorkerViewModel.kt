package com.maksimowiczm.findmyip.ui.settings.history

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.maksimowiczm.findmyip.data.AddressRefreshWorkerManager
import com.maksimowiczm.findmyip.infrastructure.di.get
import com.maksimowiczm.findmyip.infrastructure.di.observe
import com.maksimowiczm.findmyip.infrastructure.di.set
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private val intervalPreferenceKey = longPreferencesKey("interval")
private const val DEFAULT_INTERVAL = 240L

class BackgroundWorkerViewModel(
    private val dataStore: DataStore<Preferences>,
    private val workerManager: AddressRefreshWorkerManager
) : ViewModel() {
    val interval = dataStore
        .observe(intervalPreferenceKey)
        .map { it ?: DEFAULT_INTERVAL }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DEFAULT_INTERVAL
        )

    val workerStatus = workerManager.observeWorkerStatus()
        .map { infos ->
            if (infos.size > 1) {
                Log.w(TAG, "More than one worker with the same tag")
            }

            infos.firstOrNull()?.state in listOf(
                WorkInfo.State.ENQUEUED,
                WorkInfo.State.RUNNING
            )
        }.filterNotNull()

    fun setRefreshInterval(interval: Long) {
        viewModelScope.launch {
            dataStore.set(intervalPreferenceKey to interval)
            workerManager.cancelAndCreatePeriodicWorkRequest(interval)
        }
    }

    fun setEnabled(isEnabled: Boolean) {
        if (!isEnabled) {
            workerManager.cancelPeriodicWorkRequest()
        } else {
            viewModelScope.launch {
                val interval = dataStore.get(intervalPreferenceKey)

                if (interval == null) {
                    dataStore.set(intervalPreferenceKey to DEFAULT_INTERVAL)
                }

                workerManager.cancelAndCreatePeriodicWorkRequest(interval ?: DEFAULT_INTERVAL)
            }
        }
    }

    private companion object {
        const val TAG = "BackgroundWorkerViewModel"
    }
}
