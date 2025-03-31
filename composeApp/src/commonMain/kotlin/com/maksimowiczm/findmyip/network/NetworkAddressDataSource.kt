package com.maksimowiczm.findmyip.network

import kotlinx.coroutines.flow.Flow

data class NetworkAddress(val ip: String)

sealed interface AddressStatus {
    data object None : AddressStatus
    data object InProgress : AddressStatus
    data class Success(val address: NetworkAddress) : AddressStatus
    data class Error(val exception: Exception) : AddressStatus
}

sealed interface AddressRefreshResult {
    data object AlreadyInProgress : AddressRefreshResult
    data object Ok : AddressRefreshResult
}

interface NetworkAddressDataSource {
    val addressFlow: Flow<AddressStatus>
    fun refreshAddress(): AddressRefreshResult
}
