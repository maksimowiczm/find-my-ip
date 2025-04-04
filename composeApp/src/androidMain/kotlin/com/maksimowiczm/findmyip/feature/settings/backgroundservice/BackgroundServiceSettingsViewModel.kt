package com.maksimowiczm.findmyip.feature.settings.backgroundservice

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.maksimowiczm.findmyip.data.AddressRefreshWorker
import com.maksimowiczm.findmyip.data.PreferenceKeys
import com.maksimowiczm.findmyip.infrastructure.di.get
import com.maksimowiczm.findmyip.infrastructure.di.observe
import com.maksimowiczm.findmyip.infrastructure.di.set
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class BackgroundServiceSettingsViewModel(
    private val workManager: WorkManager,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _inputEnabled = MutableStateFlow(true)
    val inputEnabled = _inputEnabled.asStateFlow()

    val enabled = workManager
        .getWorkInfosByTagFlow(AddressRefreshWorker.TAG)
        .map { infos ->
            when (infos.firstOrNull()?.state) {
                null,
                WorkInfo.State.SUCCEEDED,
                WorkInfo.State.FAILED,
                WorkInfo.State.BLOCKED,
                WorkInfo.State.CANCELLED -> false

                WorkInfo.State.ENQUEUED,
                WorkInfo.State.RUNNING -> true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2_000),
            initialValue = null
        )

    fun enable() {
        viewModelScope.launch {
            _inputEnabled.emit(false)
            delay(1000L)
            AddressRefreshWorker.cancelAndCreatePeriodicWorkRequest(workManager)
            _inputEnabled.emit(true)
        }
    }

    fun disable() {
        viewModelScope.launch {
            _inputEnabled.emit(false)
            delay(1000L)
            AddressRefreshWorker.cancelPeriodicWorkRequest(workManager)
            _inputEnabled.emit(true)
        }
    }

    val notificationsEnabled = dataStore
        .observe(PreferenceKeys.notificationEnabled)
        .map { it ?: false }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2_000),
            initialValue = runBlocking {
                dataStore.get(PreferenceKeys.notificationEnabled) ?: false
            }
        )

    fun enableNotifications() {
        viewModelScope.launch {
            dataStore.set(PreferenceKeys.notificationEnabled to true)
        }
    }

    fun disableNotifications() {
        viewModelScope.launch {
            dataStore.set(PreferenceKeys.notificationEnabled to false)
        }
    }
}
