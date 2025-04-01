package com.maksimowiczm.findmyip.feature.settings.internetprotocol

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIAction
import pro.respawn.flowmvi.api.MVIIntent
import pro.respawn.flowmvi.api.MVIState

@Immutable
internal sealed interface InternetProtocolSettingsState : MVIState {
    data object Loading : InternetProtocolSettingsState
    data class Loaded(
        val canSwitchIpv4: Boolean,
        val ipv4: Boolean,
        val canSwitchIpv6: Boolean,
        val ipv6: Boolean
    ) : InternetProtocolSettingsState
}

@Immutable
internal sealed interface InternetProtocolSettingsIntent : MVIIntent {
    data class ToggleIPv4(val enabled: Boolean) : InternetProtocolSettingsIntent
    data class ToggleIPv6(val enabled: Boolean) : InternetProtocolSettingsIntent
}

@Immutable
internal sealed interface InternetProtocolSettingsAction : MVIAction
