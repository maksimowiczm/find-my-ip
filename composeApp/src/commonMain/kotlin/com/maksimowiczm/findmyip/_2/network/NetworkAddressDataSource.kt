@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.network

import com.maksimowiczm.findmyip._2.data.model.NetworkType
import kotlinx.coroutines.flow.Flow

data class NetworkAddress(val ip: String, val networkType: NetworkType?)

sealed interface AddressStatus {
    data object Loading : AddressStatus
    data class Success(val address: NetworkAddress) : AddressStatus
    data class Error(val exception: Exception) : AddressStatus
}

interface NetworkAddressDataSource {
    val providerURL: String

    fun observeAddress(): Flow<AddressStatus>
    suspend fun refreshAddress(): Result<NetworkAddress>
}
