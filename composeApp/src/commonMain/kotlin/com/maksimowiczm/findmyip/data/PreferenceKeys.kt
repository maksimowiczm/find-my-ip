package com.maksimowiczm.findmyip.data

import androidx.datastore.preferences.core.booleanPreferencesKey

object PreferenceKeys {
    val ipv4Enabled = booleanPreferencesKey("ipv4Enabled")
    val ipv6Enabled = booleanPreferencesKey("ipv6Enabled")
}
