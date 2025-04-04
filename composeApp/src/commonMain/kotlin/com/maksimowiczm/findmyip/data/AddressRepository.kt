package com.maksimowiczm.findmyip.data

import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    /**
     * Observes the address for the given [internetProtocolVersion].
     *
     * @return A [Flow] that emits the current address. It will emit null if the address fetching
     * is in progress.
     */
    fun observeAddress(internetProtocolVersion: InternetProtocolVersion): Flow<Address?>
    fun refreshAddresses()
}
