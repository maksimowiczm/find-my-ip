package com.maksimowiczm.findmyip.presentation.currentaddress

import androidx.compose.runtime.Immutable

@Immutable
sealed interface IpAddressUiState {
    val address: String?

    @Immutable data class Loading(override val address: String?) : IpAddressUiState

    @Immutable data class Success(override val address: String) : IpAddressUiState

    @Immutable data class Error(override val address: String?) : IpAddressUiState
}

@Immutable
data class CurrentAddressUiState(val ip4: IpAddressUiState, val ip6: IpAddressUiState) {
    val isError: Boolean = ip4 is IpAddressUiState.Error || ip6 is IpAddressUiState.Error
    val isLoading: Boolean = ip4 is IpAddressUiState.Loading || ip6 is IpAddressUiState.Loading
}
