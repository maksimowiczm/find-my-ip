package com.maksimowiczm.findmyip.data

import androidx.datastore.preferences.core.booleanPreferencesKey

object PreferenceKeys {
    val ipv4Enabled = booleanPreferencesKey("ipv4Enabled")
    val ipv6Enabled = booleanPreferencesKey("ipv6Enabled")

    val historyEnabled = booleanPreferencesKey("historyEnabled")

    val save_wifi_history = booleanPreferencesKey("save_wifi_history")
    val save_mobile_history = booleanPreferencesKey("save_mobile_history")
    val save_vpn_history = booleanPreferencesKey("save_vpn_history")
}
