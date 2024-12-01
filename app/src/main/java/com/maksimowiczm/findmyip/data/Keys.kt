package com.maksimowiczm.findmyip.data

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey

object Keys {
    val save_history = booleanPreferencesKey("save_history")

    val save_wifi_history = booleanPreferencesKey("save_wifi_history")
    val save_mobile_history = booleanPreferencesKey("save_mobile_history")
    val save_vpn_history = booleanPreferencesKey("save_vpn_history")

    val run_background_worker = booleanPreferencesKey("run_background_worker")
    val background_worker_interval = longPreferencesKey("background_worker_interval")

    val ipv4_enabled = booleanPreferencesKey("ipv4_enabled")
    val ipv6_enabled = booleanPreferencesKey("ipv6_enabled")
    val ip_features_tested = booleanPreferencesKey("ip_features_tested")

    /**
     * Default preferences values.
     * Should be used to explicitly define certain preferences which should not be null by default.
     */
    val defaultPreferences = mapOf<Preferences.Key<*>, Any?>(
        Pair(save_history, false),

        Pair(save_wifi_history, true),
        Pair(save_mobile_history, true),
        Pair(save_vpn_history, false),

        Pair(run_background_worker, false),

        Pair(ip_features_tested, false),
        Pair(ipv4_enabled, false),
        Pair(ipv6_enabled, false)
    )
}
