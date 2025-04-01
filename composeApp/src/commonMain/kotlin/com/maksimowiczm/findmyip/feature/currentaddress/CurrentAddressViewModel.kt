package com.maksimowiczm.findmyip.feature.currentaddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.data.AddressRepository
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

internal class CurrentAddressViewModel(private val addressRepository: AddressRepository) :
    ViewModel() {
    private val ipv4 = addressRepository
        .observeAddress(InternetProtocolVersion.IPv4)
        .map { it.toState() }

    private val ipv6 = addressRepository
        .observeAddress(InternetProtocolVersion.IPv6)
        .map { it.toState() }

    private val combined = combine(ipv4, ipv6) { ipv4, ipv6 ->
        CurrentAddressState(ipv4, ipv6)
    }

    val state = combined.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = runBlocking {
            combined.first()
        }
    )

    fun refresh() {
        viewModelScope.launch {
            addressRepository.refreshAddresses()
        }
    }
}

private fun Address.toState(): IpAddressState = when (this) {
    is Address.Error -> IpAddressState.Error(message)
    Address.Loading -> IpAddressState.Loading
    is Address.Success -> IpAddressState.Success(ip)
    is Address.Disabled -> IpAddressState.Disabled
}
