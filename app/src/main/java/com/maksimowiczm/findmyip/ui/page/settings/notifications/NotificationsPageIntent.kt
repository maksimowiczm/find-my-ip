package com.maksimowiczm.findmyip.ui.page.settings.notifications

sealed interface NotificationsPageIntent {
    data object ToggleNotifications : NotificationsPageIntent
    data object ToggleWifi : NotificationsPageIntent
    data object ToggleCellular : NotificationsPageIntent
    data object ToggleVpn : NotificationsPageIntent
    data object ToggleIpv4 : NotificationsPageIntent
    data object ToggleIpv6 : NotificationsPageIntent
}
