package com.maksimowiczm.findmyip.network

import com.maksimowiczm.findmyip.data.model.NetworkType
import kotlinx.coroutines.flow.Flow

sealed interface AddressStatus {
    data object None : AddressStatus
    data object InProgress : AddressStatus
    data class Success(val address: String, val networkType: NetworkType) : AddressStatus

    data class Error(val exception: Exception) : AddressStatus
}

sealed interface AddressRefreshResult {
    data object AlreadyInProgress : AddressRefreshResult
    data object Ok : AddressRefreshResult
}

interface NetworkAddressDataSource {
    val addressFlow: Flow<AddressStatus>
    fun refreshAddress(): AddressRefreshResult
    suspend fun blockingRefreshAddress(): Result<AddressStatus.Success>
}
