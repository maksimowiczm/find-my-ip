package com.maksimowiczm.findmyip.ui.page.settings.notifications

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.domain.preferences.NotificationPreferences.Companion.notificationsCellularEnabled
import com.maksimowiczm.findmyip.domain.preferences.NotificationPreferences.Companion.notificationsEnabled
import com.maksimowiczm.findmyip.domain.preferences.NotificationPreferences.Companion.notificationsIpv4Enabled
import com.maksimowiczm.findmyip.domain.preferences.NotificationPreferences.Companion.notificationsIpv6Enabled
import com.maksimowiczm.findmyip.domain.preferences.NotificationPreferences.Companion.notificationsVpnEnabled
import com.maksimowiczm.findmyip.domain.preferences.NotificationPreferences.Companion.notificationsWifiEnabled
import com.maksimowiczm.findmyip.ext.launch
import com.maksimowiczm.findmyip.ext.set
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NotificationsPageViewModel(private val userPreferences: DataStore<Preferences>) :
    ViewModel() {
    val state = userPreferences.data
        .map { it.toState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2_000),
            initialValue = NotificationsPageState.Disabled
        )

    fun onIntent(intent: NotificationsPageIntent): Unit = launch {
        userPreferences.set(intent.preferenceFlag to intent.newState)
    }
}

private fun Preferences.toState(): NotificationsPageState =
    if (this[notificationsEnabled] ?: false) {
        NotificationsPageState.Enabled(
            wifiEnabled = this[notificationsWifiEnabled] ?: false,
            cellularEnabled = this[notificationsCellularEnabled] ?: false,
            vpnEnabled = this[notificationsVpnEnabled] ?: false,
            ipv4Enabled = this[notificationsIpv4Enabled] ?: false,
            ipv6Enabled = this[notificationsIpv6Enabled] ?: false
        )
    } else {
        NotificationsPageState.Disabled
    }

private val NotificationsPageIntent.preferenceFlag
    get(): Preferences.Key<Boolean> = when (this) {
        is NotificationsPageIntent.ToggleCellular -> notificationsCellularEnabled
        is NotificationsPageIntent.ToggleIpv4 -> notificationsIpv4Enabled
        is NotificationsPageIntent.ToggleIpv6 -> notificationsIpv6Enabled
        is NotificationsPageIntent.ToggleNotifications -> notificationsEnabled
        is NotificationsPageIntent.ToggleVpn -> notificationsVpnEnabled
        is NotificationsPageIntent.ToggleWifi -> notificationsWifiEnabled
    }
