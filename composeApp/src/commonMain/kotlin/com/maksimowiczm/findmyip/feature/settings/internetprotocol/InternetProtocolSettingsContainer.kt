package com.maksimowiczm.findmyip.feature.settings.internetprotocol

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.maksimowiczm.findmyip.data.PreferenceKeys
import com.maksimowiczm.findmyip.infrastructure.di.observe
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.dsl.store
import pro.respawn.flowmvi.plugins.reduce
import pro.respawn.flowmvi.plugins.whileSubscribed

private typealias State = InternetProtocolSettingsState
private typealias Intent = InternetProtocolSettingsIntent
private typealias Action = InternetProtocolSettingsAction

internal class InternetProtocolSettingsContainer(private val dataStore: DataStore<Preferences>) :
    Container<State, Intent, Action> {
    override val store = store(InternetProtocolSettingsState.Loading) {
        whileSubscribed {
            combine(
                dataStore.observe(PreferenceKeys.ipv4Enabled).map { it ?: false },
                dataStore.observe(PreferenceKeys.ipv6Enabled).map { it ?: false }
            ) { ipv4, ipv6 ->
                // Can switch only when both are enabled

                updateState {
                    InternetProtocolSettingsState.Loaded(
                        canSwitchIpv4 = ipv6 || !ipv4,
                        ipv4 = ipv4,
                        canSwitchIpv6 = ipv4 || !ipv6,
                        ipv6 = ipv6
                    )
                }
            }.launchIn(this)
        }

        reduce {
            when (it) {
                is InternetProtocolSettingsIntent.ToggleIPv4 if (it.enabled) -> {
                    dataStore.edit { preferences ->
                        preferences[PreferenceKeys.ipv4Enabled] = true
                    }
                }

                is InternetProtocolSettingsIntent.ToggleIPv4 -> {
                    dataStore.edit { preferences ->
                        if (preferences[PreferenceKeys.ipv6Enabled] == true) {
                            preferences[PreferenceKeys.ipv4Enabled] = false
                        }
                    }
                }

                is InternetProtocolSettingsIntent.ToggleIPv6 if (it.enabled) -> {
                    dataStore.edit { preferences ->
                        preferences[PreferenceKeys.ipv6Enabled] = true
                    }
                }

                is InternetProtocolSettingsIntent.ToggleIPv6 -> {
                    dataStore.edit { preferences ->
                        if (preferences[PreferenceKeys.ipv4Enabled] == true) {
                            preferences[PreferenceKeys.ipv6Enabled] = false
                        }
                    }
                }
            }
        }
    }
}
