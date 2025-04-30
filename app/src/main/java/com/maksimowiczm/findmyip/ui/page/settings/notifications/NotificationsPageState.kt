package com.maksimowiczm.findmyip.ui.page.settings.notifications

import androidx.compose.runtime.Immutable

@Immutable
sealed interface NotificationsPageState {

    @Immutable
    data object Disabled : NotificationsPageState

    @Immutable
    data class Enabled(
        val wifiEnabled: Boolean,
        val cellularEnabled: Boolean,
        val vpnEnabled: Boolean,
        val ipv4Enabled: Boolean,
        val ipv6Enabled: Boolean
    ) : NotificationsPageState
}
