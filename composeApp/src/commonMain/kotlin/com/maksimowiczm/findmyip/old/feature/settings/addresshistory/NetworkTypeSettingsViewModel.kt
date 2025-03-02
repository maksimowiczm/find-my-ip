package com.maksimowiczm.findmyip.old.feature.settings.addresshistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.old.data.Keys
import com.maksimowiczm.findmyip.old.data.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NetworkTypeSettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val wifiState = userPreferencesRepository.observe(Keys.save_wifi_history)
        .map { it == true }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    val mobileState = userPreferencesRepository.observe(Keys.save_mobile_history)
        .map { it == true }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    val vpnState = userPreferencesRepository.observe(Keys.save_vpn_history)
        .map { it == true }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    fun setWifi(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.set(Keys.save_wifi_history, enabled)
        }
    }

    fun setMobile(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.set(Keys.save_mobile_history, enabled)
        }
    }

    fun setVpn(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.set(Keys.save_vpn_history, enabled)
        }
    }
}
