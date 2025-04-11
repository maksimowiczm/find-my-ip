package com.maksimowiczm.findmyip.feature.settings.history

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.data.NotificationsPreferences
import com.maksimowiczm.findmyip.data.PreferenceKeys
import com.maksimowiczm.findmyip.data.WorkerManager
import com.maksimowiczm.findmyip.infrastructure.di.observe
import com.maksimowiczm.findmyip.infrastructure.di.set
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AndroidHistorySettingsViewModel(
    private val workerManager: WorkerManager,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    init {
        viewModelScope.launch {
            dataStore.observe(PreferenceKeys.historyEnabled).collectLatest {
                if (it != true) {
                    workerManager.cancel()
                }
            }
        }
    }

    val workerEnabled = workerManager.isEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2_000),
        initialValue = runBlocking { workerManager.isEnabled.first() }
    )

    val notificationsEnabled = dataStore
        .observe(NotificationsPreferences.notificationEnabled)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2_000),
            initialValue = null
        )

    fun toggleWorker(state: Boolean) {
        if (state) {
            viewModelScope.launch {
                workerManager.start()
            }
        } else {
            workerManager.cancel()
        }
    }

    fun toggleNotifications(state: Boolean) {
        viewModelScope.launch {
            dataStore.set(NotificationsPreferences.notificationEnabled to state)
        }
    }
}
