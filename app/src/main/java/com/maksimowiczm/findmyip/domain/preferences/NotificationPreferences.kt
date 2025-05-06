package com.maksimowiczm.findmyip.domain.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.maksimowiczm.findmyip.domain.model.Address
import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.model.NetworkType
import com.maksimowiczm.findmyip.ext.get

fun interface NotificationPreferences {
    suspend fun shouldPostNotification(address: Address): Boolean

    companion object {
        val notificationsEnabled = booleanPreferencesKey("notifications_enabled")
        val notificationsWifiEnabled = booleanPreferencesKey("notifications_wifi_enabled")
        val notificationsCellularEnabled = booleanPreferencesKey("notifications_cellular_enabled")
        val notificationsVpnEnabled = booleanPreferencesKey("notifications_vpn_enabled")
        val notificationsIpv4Enabled = booleanPreferencesKey("notifications_ipv4_enabled")
        val notificationsIpv6Enabled = booleanPreferencesKey("notifications_ipv6_enabled")

        /**
         * Enables all notification preferences.
         */
        fun MutablePreferences.enableAll() {
            this[notificationsEnabled] = true
            this[notificationsWifiEnabled] = true
            this[notificationsCellularEnabled] = true
            this[notificationsVpnEnabled] = true
            this[notificationsIpv4Enabled] = true
            this[notificationsIpv6Enabled] = true
        }
    }
}

class NotificationPreferencesImpl(private val dataStore: DataStore<Preferences>) :
    NotificationPreferences {
    override suspend fun shouldPostNotification(address: Address): Boolean {
        val notificationsEnabled =
            dataStore.get(NotificationPreferences.notificationsEnabled) ?: false
        val notificationsWifiEnabled =
            dataStore.get(NotificationPreferences.notificationsWifiEnabled) ?: false
        val notificationsCellularEnabled =
            dataStore.get(NotificationPreferences.notificationsCellularEnabled) ?: false
        val notificationsVpnEnabled =
            dataStore.get(NotificationPreferences.notificationsVpnEnabled) ?: false
        val notificationsIpv4Enabled =
            dataStore.get(NotificationPreferences.notificationsIpv4Enabled) ?: false
        val notificationsIpv6Enabled =
            dataStore.get(NotificationPreferences.notificationsIpv6Enabled) ?: false

        if (!notificationsEnabled) {
            return false
        }

        val networkType = when {
            address.isWiFi && notificationsWifiEnabled -> true
            address.isCellular && notificationsCellularEnabled -> true
            address.isVPN && notificationsVpnEnabled -> true
            else -> false
        }

        val internetProtocol = when {
            address.isIPv4 && notificationsIpv4Enabled -> true
            address.isIPv6 && notificationsIpv6Enabled -> true
            else -> false
        }

        return networkType && internetProtocol
    }
}

private val Address.isIPv4: Boolean
    get() = internetProtocol == InternetProtocol.IPv4

private val Address.isIPv6: Boolean
    get() = internetProtocol == InternetProtocol.IPv6

private val Address.isWiFi: Boolean
    get() = networkType == NetworkType.WiFi

private val Address.isCellular: Boolean
    get() = networkType == NetworkType.Cellular

private val Address.isVPN: Boolean
    get() = networkType == NetworkType.VPN
