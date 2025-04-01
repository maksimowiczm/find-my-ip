package com.maksimowiczm.findmyip.feature.settings.history

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIIntent
import pro.respawn.flowmvi.api.MVIState

@Immutable
internal sealed interface HistorySettingsState : MVIState {
    data object Loading : HistorySettingsState

    sealed interface Loaded : HistorySettingsState
    data object Disabled : Loaded
    data class Enabled(
        val saveDuplicates: Boolean,
        val wifiEnabled: Boolean,
        val cellularDataEnabled: Boolean,
        val vpnEnabled: Boolean
    ) : Loaded
}

@Immutable
internal sealed interface HistorySettingsIntent : MVIIntent {
    data object EnableHistory : HistorySettingsIntent
    data object DisableHistory : HistorySettingsIntent
    data class ToggleDuplicates(val enabled: Boolean) : HistorySettingsIntent
    data class ToggleWifi(val enabled: Boolean) : HistorySettingsIntent
    data class ToggleCellularData(val enabled: Boolean) : HistorySettingsIntent
    data class ToggleVpn(val enabled: Boolean) : HistorySettingsIntent
}
