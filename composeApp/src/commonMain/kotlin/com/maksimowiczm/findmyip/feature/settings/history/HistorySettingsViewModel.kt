package com.maksimowiczm.findmyip.feature.settings.history

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.data.PreferenceKeys
import com.maksimowiczm.findmyip.data.WorkerManager
import com.maksimowiczm.findmyip.domain.ClearHistoryUseCase
import com.maksimowiczm.findmyip.infrastructure.di.observe
import com.maksimowiczm.findmyip.infrastructure.di.set
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistorySettingsViewModel(
    private val dataStore: DataStore<Preferences>,
    private val workerManager: WorkerManager,
    private val clearHistoryUseCase: ClearHistoryUseCase
) : ViewModel() {

    val state = combine(
        dataStore.observe(PreferenceKeys.historyEnabled).map { it ?: false },
        dataStore.observe(PreferenceKeys.historySaveDuplicates).map { it ?: false },
        dataStore.observe(PreferenceKeys.saveWifiHistory).map { it ?: false },
        dataStore.observe(PreferenceKeys.saveMobileHistory).map { it ?: false },
        dataStore.observe(PreferenceKeys.saveVpnHistory).map { it ?: false },
        workerManager.isEnabled,
        dataStore.observe(PreferenceKeys.notificationEnabled)
    ) {
            enabled,
            saveDuplicates,
            wifiEnabled,
            mobileEnabled,
            vpnEnabled,
            workerEnabled,
            notificationEnabled
        ->

        if (!enabled) {
            return@combine HistorySettingsState.Disabled
        }

        HistorySettingsState.Enabled(
            saveDuplicates = saveDuplicates,
            wifiEnabled = wifiEnabled,
            cellularDataEnabled = mobileEnabled,
            vpnEnabled = vpnEnabled,
            workerEnabled = workerEnabled,
            notificationEnabled = notificationEnabled
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

                is HistorySettingsIntent.ToggleNotification ->
                    dataStore.set(PreferenceKeys.notificationEnabled to intent.enabled)

                HistorySettingsIntent.ClearHistory -> clearHistoryUseCase.clearHistory()
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

private fun <T1, T2, T3, T4, T5, T6, T7, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    transform: suspend (T1, T2, T3, T4, T5, T6, T7) -> R
): Flow<R> = kotlinx.coroutines.flow.combine(
    kotlinx.coroutines.flow.combine(flow, flow2, flow3, ::Triple),
    kotlinx.coroutines.flow.combine(flow4, flow5, flow6, ::Triple),
    flow7
) { t1, t2, f7 ->
    transform(
        t1.first,
        t1.second,
        t1.third,
        t2.first,
        t2.second,
        t2.third,
        f7
    )
}
