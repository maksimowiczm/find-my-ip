package com.maksimowiczm.findmyip.settings.internetprotocolversion

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
internal class InternetProtocolVersionSettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val ipv4Enabled = userPreferencesRepository.get(Keys.ipv4_enabled).map {
        it == true
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = false
    )

    val ipv6Enabled = userPreferencesRepository.get(Keys.ipv6_enabled).map {
        it == true
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = false
    )

    fun setIpv4(value: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.set(Keys.ipv4_enabled, value)
        }
    }

    fun setIpv6(value: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.set(Keys.ipv6_enabled, value)
        }
    }
}
