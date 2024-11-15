package com.maksimowiczm.findmyip.data.repository

import com.github.michaelbull.result.Result
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.network.CurrentAddressState
import kotlinx.coroutines.flow.Flow

interface PublicAddressRepository {
    fun observeCurrentAddress(): Flow<CurrentAddressState>

    /**
     * Fetches the current address and returns it.
     *
     * @return The current address or null if the fetch failed.
     */
    suspend fun refreshCurrentAddress(): Result<Address, Unit>

    suspend fun deleteAll()

    fun observeAddressHistory(): Flow<List<Address>>

    /**
     * Inserts the given address into the database if it is different from the latest address.
     */
    suspend fun insertIfDistinct(address: Address)
}
