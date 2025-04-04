package com.maksimowiczm.findmyip.domain

import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion

interface RefreshAndGetIfLatestUseCase {
    sealed interface AddressResult {
        /**
         * Internet protocol version is disabled
         */
        data object Disabled : AddressResult

        /**
         * Address was successfully refreshed and is not the same as the previous one
         */
        data class Success(val address: Address.Success) : AddressResult

        /**
         * Address was successfully refreshed but is the same as the previous one
         */
        data class Duplicate(val address: Address.Success) : AddressResult

        data class Error(val e: Throwable?) : AddressResult
    }

    suspend fun refreshAndGetIfLatest(protocol: InternetProtocolVersion): AddressResult
}
