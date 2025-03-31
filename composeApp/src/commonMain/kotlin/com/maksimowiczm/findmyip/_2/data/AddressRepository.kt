@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.data

import androidx.paging.PagingData
import com.maksimowiczm.findmyip._2.data.model.Address
import com.maksimowiczm.findmyip._2.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip._2.data.model.NetworkType
import kotlinx.coroutines.flow.Flow

sealed interface AddressStatus {
    data object Loading : AddressStatus
    data object Disabled : AddressStatus
    data class Success(val address: Address) : AddressStatus
    data class Error(val error: Throwable?) : AddressStatus
}

interface AddressRepository {
    fun observeAddressProviderUrl(internetProtocolVersion: InternetProtocolVersion): Flow<String>

    fun observeAddress(internetProtocolVersion: InternetProtocolVersion): Flow<AddressStatus>

    suspend fun refreshAddresses()

    fun observeAddressesPaged(
        internetProtocolVersion: InternetProtocolVersion
    ): Flow<PagingData<Address>>

    suspend fun clearHistory()

    val availableNetworkTypes: List<NetworkType>
}
