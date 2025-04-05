package com.maksimowiczm.findmyip.feature.settings.history

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.data.PreferenceKeys
import com.maksimowiczm.findmyip.data.WorkerManager
import com.maksimowiczm.findmyip.infrastructure.di.observe
import com.maksimowiczm.findmyip.infrastructure.di.set
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistorySettingsViewModel(
    private val dataStore: DataStore<Preferences>,
    private val workerManager: WorkerManager
) : ViewModel() {

    val state = combine(
        dataStore.observe(PreferenceKeys.historyEnabled).map { it ?: false },
        dataStore.observe(PreferenceKeys.historySaveDuplicates).map { it ?: false },
        dataStore.observe(PreferenceKeys.saveWifiHistory).map { it ?: false },
        dataStore.observe(PreferenceKeys.saveMobileHistory).map { it ?: false },
        dataStore.observe(PreferenceKeys.saveVpnHistory).map { it ?: false },
        workerManager.isEnabled
    ) {
        val enabled = it[0]
        val saveDuplicates = it[1]
        val wifiEnabled = it[2]
        val mobileEnabled = it[3]
        val vpnEnabled = it[4]
        val workerEnabled = it[5]

        if (!enabled) {
            return@combine HistorySettingsState.Disabled
        }

        HistorySettingsState.Enabled(
            saveDuplicates = saveDuplicates,
            wifiEnabled = wifiEnabled,
            cellularDataEnabled = mobileEnabled,
            vpnEnabled = vpnEnabled,
            workerEnabled = workerEnabled
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2000),
        initialValue = HistorySettingsState.Loading
    )

    fun onIntent(intent: HistorySettingsIntent) {
        viewModelScope.launch {
            when (intent) {
                is HistorySettingsIntent.EnableHistory -> dataStore.enableHistory()

                is HistorySettingsIntent.DisableHistory -> {
                    workerManager.cancel()
                    dataStore.set(PreferenceKeys.historyEnabled to false)
                }

                is HistorySettingsIntent.ToggleDuplicates ->
                    dataStore.set(PreferenceKeys.historySaveDuplicates to intent.enabled)

                is HistorySettingsIntent.ToggleCellularData ->
                    dataStore.set(PreferenceKeys.saveMobileHistory to intent.enabled)

                is HistorySettingsIntent.ToggleVpn ->
                    dataStore.set(PreferenceKeys.saveVpnHistory to intent.enabled)

                is HistorySettingsIntent.ToggleWifi ->
                    dataStore.set(PreferenceKeys.saveWifiHistory to intent.enabled)

                is HistorySettingsIntent.ToggleWorker -> {
                    if (intent.enabled) {
                        workerManager.start()
                    } else {
                        workerManager.cancel()
                    }
                }
            }
        }
    }
}

private suspend fun DataStore<Preferences>.enableHistory() {
    edit { prefs ->
        prefs[PreferenceKeys.historyEnabled] = true

        prefs[PreferenceKeys.saveWifiHistory].apply {
            if (this == null) {
                prefs[PreferenceKeys.saveWifiHistory] = true
                prefs[PreferenceKeys.saveMobileHistory] = true
                prefs[PreferenceKeys.saveVpnHistory] = true
            }
        }
    }
}
