@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.data.initializer

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.maksimowiczm.findmyip.data.PreferenceKeys

/**
 * Initializes the network type preferences. This is done only once. If the preferences are already
 * initialized, this initializer does nothing.
 *
 * Enable saving history for all network types by default.
 */
class NetworkTypeInitializer(private val dataStore: DataStore<Preferences>) : Initializer {
    override suspend fun invoke() {
        dataStore.edit { preferences ->
            if (preferences[PreferenceKeys.networkTypeInitialized] == true) {
                return@edit
            }

            preferences[PreferenceKeys.networkTypeInitialized] = true

            preferences[PreferenceKeys.saveWifiHistory] = true
            preferences[PreferenceKeys.saveMobileHistory] = true
            preferences[PreferenceKeys.saveVpnHistory] = true
        }
    }
}
