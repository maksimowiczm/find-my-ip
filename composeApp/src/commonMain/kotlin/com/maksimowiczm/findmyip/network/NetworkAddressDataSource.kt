package com.maksimowiczm.findmyip.network

import com.maksimowiczm.findmyip.data.model.NetworkType
import kotlinx.coroutines.flow.Flow

data class NetworkAddress(val ip: String, val networkType: NetworkType?)

interface NetworkAddressDataSource {
    fun observeAddress(): Flow<Result<NetworkAddress?>>
    suspend fun refreshAddress(): Result<NetworkAddress>
}
