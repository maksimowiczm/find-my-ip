package com.maksimowiczm.findmyip.domain

import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import kotlinx.coroutines.flow.Flow

interface ObserveAddressUseCase {

    sealed interface AddressStatus {
        data object Loading : AddressStatus
        data object Disabled : AddressStatus
        data class Success(val address: Address) : AddressStatus
        data class Error(val e: Throwable) : AddressStatus
    }

    fun observeAddress(protocol: InternetProtocolVersion): Flow<AddressStatus>
}
