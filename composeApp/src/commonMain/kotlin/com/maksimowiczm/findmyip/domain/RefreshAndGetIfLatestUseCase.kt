package com.maksimowiczm.findmyip.domain

import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion

interface RefreshAndGetIfLatestUseCase {
    sealed interface AddressResult {
        data object Disabled : AddressResult
        data class Success(val address: Address.Success) : AddressResult
        data object Skipped : AddressResult
        data class Error(val e: Throwable?) : AddressResult
    }

    suspend fun refreshAndGetIfLatest(
        internetProtocolVersion: InternetProtocolVersion
    ): AddressResult
}
