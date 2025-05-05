package com.maksimowiczm.findmyip.domain.source

import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.model.NetworkType
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

data class NetworkAddress(
    val ip: String,
    val networkType: NetworkType,
    val internetProtocol: InternetProtocol,
    val dateTime: LocalDateTime
)

sealed interface AddressState {
    data object Refreshing : AddressState
    data class Success(val address: NetworkAddress) : AddressState
    data class Error(val error: Throwable?) : AddressState
}

interface AddressObserver {
    val flow: Flow<AddressState>

    suspend fun refresh(): Result<NetworkAddress>
}
