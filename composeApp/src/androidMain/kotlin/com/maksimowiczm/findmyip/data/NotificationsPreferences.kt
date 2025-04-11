package com.maksimowiczm.findmyip.data

import androidx.datastore.preferences.core.booleanPreferencesKey

object NotificationsPreferences {
    val notificationEnabled = booleanPreferencesKey("notificationEnabled")
}
