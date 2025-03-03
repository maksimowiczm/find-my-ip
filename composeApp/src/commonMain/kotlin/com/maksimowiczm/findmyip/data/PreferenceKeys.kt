package com.maksimowiczm.findmyip.data

import androidx.datastore.preferences.core.booleanPreferencesKey

object PreferenceKeys {
    val ipv4Enabled = booleanPreferencesKey("ipv4Enabled")
    val ipv6Enabled = booleanPreferencesKey("ipv6Enabled")

    val historyEnabled = booleanPreferencesKey("historyEnabled")

    val networkTypeInitialized = booleanPreferencesKey("networkTypeInitialized")
    val saveWifiHistory = booleanPreferencesKey("saveWifiHistory")
    val saveMobileHistory = booleanPreferencesKey("saveMobileHistory")
    val saveVpnHistory = booleanPreferencesKey("saveVpnHistory")

    val ipFeaturesTested = booleanPreferencesKey("ipFeaturesTested")

    val hideMigrationDialog = booleanPreferencesKey("hideMigrationDialog")
}
