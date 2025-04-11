package com.maksimowiczm.findmyip.feature.currentaddress

import androidx.compose.runtime.Immutable

@Immutable
internal data class CurrentAddressState(val ipv4: IpAddressState, val ipv6: IpAddressState)

@Immutable
internal sealed interface IpAddressState {
    data object Loading : IpAddressState
    data object Disabled : IpAddressState
    data class Success(val ip: String) : IpAddressState
    data class Error(val message: String) : IpAddressState
}
