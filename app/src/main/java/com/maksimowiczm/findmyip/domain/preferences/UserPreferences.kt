package com.maksimowiczm.findmyip.domain.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

typealias UserPreferencesManager = DataStore<Preferences>

fun <T> DataStore<Preferences>.observe(key: Preferences.Key<T>): Flow<T?> =
    data.map { preferences ->
        preferences[key]
    }

suspend fun <T> DataStore<Preferences>.get(key: Preferences.Key<T>): T? = observe(key).first()

suspend fun DataStore<Preferences>.set(vararg pairs: Preferences.Pair<*>) {
    edit { preferences ->
        preferences.putAll(*pairs)
    }
}

object UserPreferences {
    val notificationsEnabled = booleanPreferencesKey("notifications_enabled")
    val notificationsWifiEnabled = booleanPreferencesKey("notifications_wifi_enabled")
    val notificationsCellularEnabled = booleanPreferencesKey("notifications_cellular_enabled")
    val notificationsVpnEnabled = booleanPreferencesKey("notifications_vpn_enabled")
    val notificationsIpv4Enabled = booleanPreferencesKey("notifications_ipv4_enabled")
    val notificationsIpv6Enabled = booleanPreferencesKey("notifications_ipv6_enabled")
}
