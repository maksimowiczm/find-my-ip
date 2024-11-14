package com.maksimowiczm.findmyip.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey

object Keys {
    val save_history = booleanPreferencesKey("save_history")

    val run_background_worker = booleanPreferencesKey("run_background_worker")
    val background_worker_interval = longPreferencesKey("background_worker_interval")
}
