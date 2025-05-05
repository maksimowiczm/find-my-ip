package com.maksimowiczm.findmyip.domain.source

import com.maksimowiczm.findmyip.data.model.AddressEntity
import kotlinx.coroutines.flow.Flow

interface AddressLocalDataSource {
    /**
     * Observes addresses in the database that match the given query.
     *
     * @param query The search query to filter addresses.
     * @param start The start timestamp for filtering addresses.
     * @param end The end timestamp for filtering addresses.
     */
    fun observeAddresses(query: String, start: Long?, end: Long?): Flow<List<AddressEntity>>

    suspend fun getAddress(id: Long): AddressEntity?

    /**
     * Inserts an address into the database if it is unique to the last address. This means that if
     * the address already exists in the database, it will not be inserted again.
     */
    suspend fun insertAddressIfUniqueToLast(address: AddressEntity): AddressEntity?

    suspend fun deleteAddress(address: AddressEntity)
}
