package com.maksimowiczm.findmyip.feature.settings.backgroundservice

import androidx.compose.runtime.Immutable
import pro.respawn.flowmvi.api.MVIIntent
import pro.respawn.flowmvi.api.MVIState

@Immutable
internal sealed interface BackgroundServiceSettingsState : MVIState {
    data object Loading : BackgroundServiceSettingsState
    data object Disabled : BackgroundServiceSettingsState
    data object Enabling : BackgroundServiceSettingsState
    data object Enabled : BackgroundServiceSettingsState
    data object Disabling : BackgroundServiceSettingsState
}

@Immutable
internal sealed interface BackgroundServiceSettingsIntent : MVIIntent {
    data object Enable : BackgroundServiceSettingsIntent
    data object Disable : BackgroundServiceSettingsIntent
}
