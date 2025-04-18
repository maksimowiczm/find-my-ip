package com.maksimowiczm.findmyip.feature.settings.history

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.data.PreferenceKeys
import com.maksimowiczm.findmyip.domain.ClearHistoryUseCase
import com.maksimowiczm.findmyip.infrastructure.di.observe
import com.maksimowiczm.findmyip.infrastructure.di.set
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistorySettingsViewModel(
    private val dataStore: DataStore<Preferences>,
    private val clearHistoryUseCase: ClearHistoryUseCase
) : ViewModel() {

    val state = combine(
        dataStore.observe(PreferenceKeys.historyEnabled).map { it ?: false },
        dataStore.observe(PreferenceKeys.historySaveDuplicates).map { it ?: false },
        dataStore.observe(PreferenceKeys.saveWifiHistory).map { it ?: false },
        dataStore.observe(PreferenceKeys.saveMobileHistory).map { it ?: false },
        dataStore.observe(PreferenceKeys.saveVpnHistory).map { it ?: false }
    ) { enabled, saveDuplicates, wifiEnabled, mobileEnabled, vpnEnabled ->

        if (!enabled) {
            return@combine HistorySettingsState.Disabled
        }

        HistorySettingsState.Enabled(
            saveDuplicates = saveDuplicates,
            wifiEnabled = wifiEnabled,
            cellularDataEnabled = mobileEnabled,
            vpnEnabled = vpnEnabled
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
