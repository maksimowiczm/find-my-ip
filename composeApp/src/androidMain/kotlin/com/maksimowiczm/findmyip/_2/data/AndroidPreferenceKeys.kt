@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.data

import androidx.datastore.preferences.core.booleanPreferencesKey

object AndroidPreferenceKeys {
    val autoRefreshSettingsEnabledKey = booleanPreferencesKey("autoRefreshSettingsEnabled")
}
