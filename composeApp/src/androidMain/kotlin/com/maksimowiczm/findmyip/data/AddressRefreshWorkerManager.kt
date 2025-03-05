package com.maksimowiczm.findmyip.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.work.WorkManager
import com.maksimowiczm.findmyip.infrastructure.di.observe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Must be eagerly initialized singleton to observe changes in the historyEnabled preference.
 */
class AddressRefreshWorkerManager(
    private val context: Context,
    private val dataStore: DataStore<Preferences>
) {
    private val workManager
        get() = WorkManager.getInstance(context)

    /**
     * Launches a task that observes the historyEnabled preference and cancels the periodic work
     * request if it is disabled.
     */
    fun CoroutineScope.launchCancellationTask() {
        dataStore.observe(PreferenceKeys.historyEnabled).filterNotNull().onEach {
            if (!it) {
                cancelPeriodicWorkRequest()
            }
        }.launchIn(this)
    }

    fun observeWorkerStatus() = workManager.getWorkInfosByTagFlow(AddressRefreshWorker.TAG)

    suspend fun cancelAndCreatePeriodicWorkRequest(intervalInMinutes: Long) {
        AddressRefreshWorker.cancelAndCreatePeriodicWorkRequest(workManager, intervalInMinutes)
    }

    fun cancelPeriodicWorkRequest() {
        AddressRefreshWorker.cancelPeriodicWorkRequest(workManager)
    }
}
