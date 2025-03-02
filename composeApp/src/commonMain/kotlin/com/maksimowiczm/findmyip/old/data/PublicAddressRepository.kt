package com.maksimowiczm.findmyip.old.data

import com.github.michaelbull.result.Result
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.old.data.model.Address
import com.maksimowiczm.findmyip.old.network.CurrentAddressState
import kotlinx.coroutines.flow.Flow

interface PublicAddressRepository {
    fun observeCurrentAddress(
        internetProtocolVersion: InternetProtocolVersion
    ): Flow<CurrentAddressState>

    /**
     * Fetches the current address and returns it.
     */
    suspend fun refreshCurrentAddress(
        internetProtocolVersion: InternetProtocolVersion
    ): Result<Address, Unit>

    suspend fun deleteAll()

    fun observeAddressHistory(internetProtocolVersion: InternetProtocolVersion): Flow<List<Address>>

    /**
     * Inserts the given address into the database if it is different from the latest address.
     */
    suspend fun insertIfDistinct(address: Address)
}
