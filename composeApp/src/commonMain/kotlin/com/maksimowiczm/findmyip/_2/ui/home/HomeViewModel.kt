@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.ui.home

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip._2.data.AddressRepository
import com.maksimowiczm.findmyip._2.data.AddressStatus
import com.maksimowiczm.findmyip._2.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip._2.infrastructure.di.observe
import com.maksimowiczm.findmyip.data.PreferenceKeys
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val addressRepository: AddressRepository,
    dataStore: DataStore<Preferences>
) : ViewModel() {
    val ipv4Address: StateFlow<AddressStatus> = combine(
        dataStore.observe(PreferenceKeys.ipFeaturesTested),
        addressRepository.observeAddress(
            internetProtocolVersion = InternetProtocolVersion.IPv4
        )
    ) { tested, address ->
        if (tested == true) {
            address
        } else {
            // IPv4 is the most popular, so show loading while testing.
            AddressStatus.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = AddressStatus.Loading
    )

    val ipv6Address: StateFlow<AddressStatus> = combine(
        dataStore.observe(PreferenceKeys.ipFeaturesTested),
        addressRepository.observeAddress(
            internetProtocolVersion = InternetProtocolVersion.IPv6
        )
    ) { tested, address ->
        if (tested == true) {
            address
        } else {
            // IPv6 is not as popular as IPv4, so disable it while testing.
            AddressStatus.Disabled
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = AddressStatus.Loading
    )

    fun onRefresh() {
        viewModelScope.launch {
            addressRepository.refreshAddresses()
        }
    }
}
