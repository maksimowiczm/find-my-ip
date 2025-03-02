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
    fun observeAddress(internetProtocolVersion: InternetProtocolVersion): Flow<AddressStatus>

    fun refreshAddresses()

    fun observeAddressesPaged(
        internetProtocolVersion: InternetProtocolVersion
    ): Flow<PagingData<Address>>
}
