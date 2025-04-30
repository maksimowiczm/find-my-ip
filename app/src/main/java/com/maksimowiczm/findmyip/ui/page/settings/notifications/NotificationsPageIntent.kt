package com.maksimowiczm.findmyip.ui.page.settings.notifications

sealed interface NotificationsPageIntent {
    data class ToggleNotifications(val newState: Boolean) : NotificationsPageIntent
    data class ToggleWifi(val newState: Boolean) : NotificationsPageIntent
    data class ToggleCellular(val newState: Boolean) : NotificationsPageIntent
    data class ToggleVpn(val newState: Boolean) : NotificationsPageIntent
    data class ToggleIpv4(val newState: Boolean) : NotificationsPageIntent
    data class ToggleIpv6(val newState: Boolean) : NotificationsPageIntent
}
