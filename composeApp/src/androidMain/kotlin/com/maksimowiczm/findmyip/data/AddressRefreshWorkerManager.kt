package com.maksimowiczm.findmyip.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.work.WorkManager
import com.maksimowiczm.findmyip.infrastructure.di.observe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Must be eagerly initialized singleton to observe changes in the historyEnabled preference.
 */
class AddressRefreshWorkerManager(
    private val context: Context,
    dataStore: DataStore<Preferences>,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    private val workManager
        get() = WorkManager.getInstance(context)

    init {
        dataStore.observe(PreferenceKeys.historyEnabled).filterNotNull().onEach {
            if (!it) {
                cancelPeriodicWorkRequest()
            }
        }.launchIn(CoroutineScope(dispatcher))
    }

    fun observeWorkerStatus() = workManager.getWorkInfosByTagFlow(AddressRefreshWorker.TAG)

    fun cancelAndCreatePeriodicWorkRequest(intervalInMinutes: Long) {
        AddressRefreshWorker.cancelAndCreatePeriodicWorkRequest(workManager, intervalInMinutes)
    }

    fun cancelPeriodicWorkRequest() {
        AddressRefreshWorker.cancelPeriodicWorkRequest(workManager)
    }
}
