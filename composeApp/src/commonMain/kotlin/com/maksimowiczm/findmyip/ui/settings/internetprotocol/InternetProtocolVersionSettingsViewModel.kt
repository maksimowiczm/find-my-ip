package com.maksimowiczm.findmyip.ui.settings.internetprotocol

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.data.AddressRepository
import com.maksimowiczm.findmyip.data.PreferenceKeys
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.infrastructure.di.observe
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InternetProtocolVersionSettingsViewModel(
    private val dataStore: DataStore<Preferences>,
    addressRepository: AddressRepository
) : ViewModel() {
    val ipv4Provider = addressRepository.observeAddressProviderUrl(InternetProtocolVersion.IPv4)
    val ipv6Provider = addressRepository.observeAddressProviderUrl(InternetProtocolVersion.IPv6)

    val ipv4Enabled = dataStore
        .observe(PreferenceKeys.ipv4Enabled)
        .map { it ?: false }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2_000),
            initialValue = null
        )

    val ipv6Enabled = dataStore
        .observe(PreferenceKeys.ipv6Enabled)
        .map { it ?: false }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2_000),
            initialValue = null
        )

    fun onIpv4EnabledChanged(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit {
                val ipv6 = it[PreferenceKeys.ipv6Enabled] ?: false

                if (!ipv6 && !enabled) {
                    return@edit
                }

                it[PreferenceKeys.ipv4Enabled] = enabled
            }
        }
    }

    fun onIpv6EnabledChanged(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit {
                val ipv4 = it[PreferenceKeys.ipv4Enabled] ?: false

                if (!ipv4 && !enabled) {
                    return@edit
                }

                it[PreferenceKeys.ipv6Enabled] = enabled
            }
        }
    }
}
