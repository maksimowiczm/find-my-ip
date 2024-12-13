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
    // Store ui state in memory to avoid race conditions
    private var localIpv4Enabled: Boolean
    private var localIpv6Enabled: Boolean

    init {
        localIpv4Enabled = userPreferencesRepository.get(Keys.ipv4_enabled) ?: false
        localIpv6Enabled = userPreferencesRepository.get(Keys.ipv6_enabled) ?: false
    }

    val ipv4Enabled = userPreferencesRepository.observe(Keys.ipv4_enabled)
        .map { it ?: false }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), localIpv4Enabled)

    val ipv6Enabled = userPreferencesRepository.observe(Keys.ipv6_enabled)
        .map { it ?: false }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), localIpv6Enabled)

    fun setIpv4(value: Boolean) {
        if (!value && !localIpv6Enabled) {
            return
        }

        localIpv4Enabled = value
        setIpFeatures()
    }

    fun setIpv6(value: Boolean) {
        if (!value && !localIpv4Enabled) {
            return
        }

        localIpv6Enabled = value
        setIpFeatures()
    }

    private fun setIpFeatures() {
        viewModelScope.launch {
            userPreferencesRepository.set(
                Keys.ipv4_enabled to localIpv4Enabled,
                Keys.ipv6_enabled to localIpv6Enabled
            )
        }
    }
}
