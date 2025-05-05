package com.maksimowiczm.findmyip.ui.page.settings.notifications

sealed interface NotificationsPageIntent {
    val newState: Boolean

    data class ToggleNotifications(override val newState: Boolean) : NotificationsPageIntent
    data class ToggleWifi(override val newState: Boolean) : NotificationsPageIntent
    data class ToggleCellular(override val newState: Boolean) : NotificationsPageIntent
    data class ToggleVpn(override val newState: Boolean) : NotificationsPageIntent
    data class ToggleIpv4(override val newState: Boolean) : NotificationsPageIntent
    data class ToggleIpv6(override val newState: Boolean) : NotificationsPageIntent
}
