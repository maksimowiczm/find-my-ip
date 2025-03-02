package com.maksimowiczm.findmyip.network

import com.github.michaelbull.result.Result
import com.maksimowiczm.findmyip.data.model.Address
import kotlinx.coroutines.flow.Flow

sealed interface CurrentAddressState {
    object None : CurrentAddressState
    object Loading : CurrentAddressState
    data class Success(val address: Address) : CurrentAddressState
    data object NetworkUnavailable : CurrentAddressState
    data class Error(val error: Throwable) : CurrentAddressState
}

expect class CurrentAddressDataSource {
    fun observeCurrentAddress(autoFetch: Boolean): Flow<CurrentAddressState>

    suspend fun refreshCurrentAddress(): Result<Address, Unit>
}
