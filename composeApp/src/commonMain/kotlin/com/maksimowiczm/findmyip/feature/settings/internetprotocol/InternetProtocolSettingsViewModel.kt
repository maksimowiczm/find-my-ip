package com.maksimowiczm.findmyip.feature.settings.internetprotocol

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.data.PreferenceKeys
import com.maksimowiczm.findmyip.domain.TestInternetProtocolsUseCase
import com.maksimowiczm.findmyip.infrastructure.di.observe
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

internal class InternetProtocolSettingsViewModel(
    private val dataStore: DataStore<Preferences>,
    private val testInternetProtocolsUseCase: TestInternetProtocolsUseCase
) : ViewModel() {
    private val _eventBus = Channel<InternetProtocolSettingsEvent>()
    val eventBus = _eventBus.receiveAsFlow()

    private val _state = combine(
        dataStore.observe(PreferenceKeys.ipv4Enabled).map { it ?: false },
        dataStore.observe(PreferenceKeys.ipv6Enabled).map { it ?: false }
    ) { ipv4, ipv6 ->
        // Can switch only when both are enabled

        InternetProtocolSettingsState(
            canSwitchIpv4 = ipv6 || !ipv4,
            ipv4 = ipv4,
            canSwitchIpv6 = ipv4 || !ipv6,
            ipv6 = ipv6
        )
    }

    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = runBlocking { _state.first() }
    )

    fun toggleIPv4(enabled: Boolean) {
        viewModelScope.launch {
            if (enabled) {
                dataStore.edit { preferences ->
                    preferences[PreferenceKeys.ipv4Enabled] = true
                }
            } else {
                dataStore.edit { preferences ->
                    if (preferences[PreferenceKeys.ipv6Enabled] == true) {
                        preferences[PreferenceKeys.ipv4Enabled] = false
                    }
                }
            }
        }
    }

    fun toggleIPv6(enabled: Boolean) {
        viewModelScope.launch {
            if (enabled) {
                dataStore.edit { preferences ->
                    preferences[PreferenceKeys.ipv6Enabled] = true
                }
            } else {
                dataStore.edit { preferences ->
                    if (preferences[PreferenceKeys.ipv4Enabled] == true) {
                        preferences[PreferenceKeys.ipv6Enabled] = false
                    }
                }
            }
        }
    }

    fun testInternetProtocols() {
        viewModelScope.launch {
            _eventBus.send(InternetProtocolSettingsEvent.StartTest)
            testInternetProtocolsUseCase.testInternetProtocols()
            _eventBus.send(InternetProtocolSettingsEvent.StopTest)
        }
    }
}
