package com.maksimowiczm.findmyip.settings.addresshistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.data.Keys
import com.maksimowiczm.findmyip.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class NetworkTypeSettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val wifiState = userPreferencesRepository.get(Keys.save_wifi_history)
        .map { it == true }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    val mobileState = userPreferencesRepository.get(Keys.save_mobile_history)
        .map { it == true }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    val vpnState = userPreferencesRepository.get(Keys.save_vpn_history)
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
