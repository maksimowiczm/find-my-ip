@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.ui.settings.history

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip._2.data.AddressRepository
import com.maksimowiczm.findmyip._2.data.model.NetworkType
import com.maksimowiczm.findmyip._2.infrastructure.di.get
import com.maksimowiczm.findmyip._2.infrastructure.di.observe
import com.maksimowiczm.findmyip._2.infrastructure.di.set
import com.maksimowiczm.findmyip.data.PreferenceKeys
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HistorySettingsViewModel(
    private val dataStore: DataStore<Preferences>,
    private val addressRepository: AddressRepository
) : ViewModel() {
    val isEnabled = dataStore
        .observe(PreferenceKeys.historyEnabled)
        .map { it ?: false }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2_000),
            initialValue = runBlocking { dataStore.get(PreferenceKeys.historyEnabled) ?: false }
        )

    val saveDuplicates = dataStore
        .observe(PreferenceKeys.historySaveDuplicates)
        .map { it ?: false }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2_000),
            initialValue = runBlocking { dataStore.get(PreferenceKeys.historyEnabled) ?: false }
        )

    private val availableNetworkTypes = addressRepository.availableNetworkTypes

    val networkTypeSettings: StateFlow<Map<NetworkType, Boolean>> = combine(
        dataStore.observe(PreferenceKeys.saveWifiHistory),
        dataStore.observe(PreferenceKeys.saveMobileHistory),
        dataStore.observe(PreferenceKeys.saveVpnHistory)
    ) { wifi, mobile, vpn ->
        availableNetworkTypes.associateWith {
            when (it) {
                NetworkType.WIFI -> wifi ?: false
                NetworkType.MOBILE -> mobile ?: false
                NetworkType.VPN -> vpn ?: false
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2_000),
        initialValue = runBlocking {
            availableNetworkTypes.associateWith {
                when (it) {
                    NetworkType.WIFI -> dataStore.get(PreferenceKeys.saveWifiHistory) ?: false
                    NetworkType.MOBILE -> dataStore.get(PreferenceKeys.saveMobileHistory) ?: false
                    NetworkType.VPN -> dataStore.get(PreferenceKeys.saveVpnHistory) ?: false
                }
            }
        }
    )

    fun onEnableChange(isEnabled: Boolean) {
        viewModelScope.launch {
            dataStore.set(PreferenceKeys.historyEnabled to isEnabled)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            addressRepository.clearHistory()
        }
    }

    fun onSaveDuplicatesChange(saveDuplicates: Boolean) {
        viewModelScope.launch {
            dataStore.set(PreferenceKeys.historySaveDuplicates to saveDuplicates)
        }
    }

    fun onNetworkTypeToggle(networkType: NetworkType, value: Boolean) {
        viewModelScope.launch {
            when (networkType) {
                NetworkType.WIFI -> dataStore.set(PreferenceKeys.saveWifiHistory to value)
                NetworkType.MOBILE -> dataStore.set(PreferenceKeys.saveMobileHistory to value)
                NetworkType.VPN -> dataStore.set(PreferenceKeys.saveVpnHistory to value)
            }
        }
    }
}
