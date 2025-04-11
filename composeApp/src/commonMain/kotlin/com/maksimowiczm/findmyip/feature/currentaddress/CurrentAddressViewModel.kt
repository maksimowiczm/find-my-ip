package com.maksimowiczm.findmyip.feature.currentaddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.domain.ObserveAddressUseCase
import com.maksimowiczm.findmyip.domain.ObserveAddressUseCase.AddressStatus
import com.maksimowiczm.findmyip.domain.RefreshAddressesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking

internal class CurrentAddressViewModel(
    observeAddressUseCase: ObserveAddressUseCase,
    private val refreshAddressesUseCase: RefreshAddressesUseCase
) : ViewModel() {
    private val ipv4 = observeAddressUseCase
        .observeAddress(InternetProtocolVersion.IPv4)
        .map { it.toState() }

    private val ipv6 = observeAddressUseCase
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
        refreshAddressesUseCase.refreshAddresses()
    }
}

private fun AddressStatus.toState(): IpAddressState = when (this) {
    is AddressStatus.Loading -> IpAddressState.Loading
    is AddressStatus.Error -> IpAddressState.Error(e.message ?: "Unknown error")
    is AddressStatus.Success -> IpAddressState.Success(address.ip)
    is AddressStatus.Disabled -> IpAddressState.Disabled
}
