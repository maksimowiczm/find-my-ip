package com.maksimowiczm.findmyip.data

import androidx.paging.PagingData
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import kotlinx.coroutines.flow.Flow

sealed interface AddressStatus {
    data object Loading : AddressStatus
    data object Disabled : AddressStatus
    data class Success(val address: Address) : AddressStatus
    data class Error(val error: Throwable?) : AddressStatus
}

interface AddressRepository {
    /**
     * Observe the current address status and persist the history if enabled.
     */
    fun observeAddress(internetProtocolVersion: InternetProtocolVersion): Flow<AddressStatus>

    suspend fun refreshAddresses()

    suspend fun refreshAddress(internetProtocolVersion: InternetProtocolVersion): AddressStatus

    fun observeAddressesPaged(
        internetProtocolVersion: InternetProtocolVersion
    ): Flow<PagingData<Address>>

    suspend fun clearHistory()
}
