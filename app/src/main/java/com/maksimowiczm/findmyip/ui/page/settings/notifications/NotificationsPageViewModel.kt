package com.maksimowiczm.findmyip.ui.page.settings.notifications

import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.domain.preferences.UserPreferences
import com.maksimowiczm.findmyip.domain.preferences.UserPreferencesManager
import com.maksimowiczm.findmyip.domain.preferences.set
import com.maksimowiczm.findmyip.ext.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NotificationsPageViewModel(private val userPreferences: UserPreferencesManager) :
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
    if (this[UserPreferences.notificationsEnabled] ?: false) {
        NotificationsPageState.Enabled(
            wifiEnabled = this[UserPreferences.notificationsWifiEnabled] ?: false,
            cellularEnabled = this[UserPreferences.notificationsCellularEnabled] ?: false,
            vpnEnabled = this[UserPreferences.notificationsVpnEnabled] ?: false,
            ipv4Enabled = this[UserPreferences.notificationsIpv4Enabled] ?: false,
            ipv6Enabled = this[UserPreferences.notificationsIpv6Enabled] ?: false
        )
    } else {
        NotificationsPageState.Disabled
    }

private val NotificationsPageIntent.preferenceFlag
    get(): Preferences.Key<Boolean> = when (this) {
        is NotificationsPageIntent.ToggleCellular -> UserPreferences.notificationsCellularEnabled
        is NotificationsPageIntent.ToggleIpv4 -> UserPreferences.notificationsIpv4Enabled
        is NotificationsPageIntent.ToggleIpv6 -> UserPreferences.notificationsIpv6Enabled
        is NotificationsPageIntent.ToggleNotifications -> UserPreferences.notificationsEnabled
        is NotificationsPageIntent.ToggleVpn -> UserPreferences.notificationsVpnEnabled
        is NotificationsPageIntent.ToggleWifi -> UserPreferences.notificationsWifiEnabled
    }
