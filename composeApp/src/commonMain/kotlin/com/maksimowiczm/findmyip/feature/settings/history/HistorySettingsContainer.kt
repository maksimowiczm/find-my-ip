package com.maksimowiczm.findmyip.feature.settings.history

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.maksimowiczm.findmyip.data.PreferenceKeys
import com.maksimowiczm.findmyip.infrastructure.di.observe
import com.maksimowiczm.findmyip.infrastructure.di.set
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.MVIAction
import pro.respawn.flowmvi.dsl.store
import pro.respawn.flowmvi.plugins.reduce
import pro.respawn.flowmvi.plugins.whileSubscribed

private typealias State = HistorySettingsState
private typealias Intent = HistorySettingsIntent
private typealias Action = MVIAction

internal class HistorySettingsContainer(private val dataStore: DataStore<Preferences>) :
    Container<State, Intent, Action> {
    override val store = store(HistorySettingsState.Loading) {
        whileSubscribed {
            combine(
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
            }.onEach { state ->
                updateState {
                    state
                }
            }.launchIn(this)
        }

        reduce {
            when (it) {
                is HistorySettingsIntent.EnableHistory -> dataStore.enableHistory()

                is HistorySettingsIntent.DisableHistory -> {
                    dataStore.set(PreferenceKeys.historyEnabled to false)
                }

                is HistorySettingsIntent.ToggleDuplicates -> {
                    dataStore.set(PreferenceKeys.historySaveDuplicates to it.enabled)
                }

                is HistorySettingsIntent.ToggleCellularData -> {
                    dataStore.set(PreferenceKeys.saveMobileHistory to it.enabled)
                }

                is HistorySettingsIntent.ToggleVpn -> {
                    dataStore.set(PreferenceKeys.saveVpnHistory to it.enabled)
                }

                is HistorySettingsIntent.ToggleWifi -> {
                    dataStore.set(PreferenceKeys.saveWifiHistory to it.enabled)
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
            }
        }
    }
}
