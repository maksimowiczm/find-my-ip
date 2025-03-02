package com.maksimowiczm.findmyip.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.data.AddressRepository
import com.maksimowiczm.findmyip.data.AddressStatus
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(private val addressRepository: AddressRepository) : ViewModel() {
    val ipv4Address: StateFlow<AddressStatus> = addressRepository.observeAddress(
        internetProtocolVersion = InternetProtocolVersion.IPv4
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = AddressStatus.Loading
    )

    val ipv6Address: StateFlow<AddressStatus> = addressRepository.observeAddress(
        internetProtocolVersion = InternetProtocolVersion.IPv6
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = AddressStatus.Loading
    )

    fun onRefresh() {
        addressRepository.refreshAddresses()
    }
}
