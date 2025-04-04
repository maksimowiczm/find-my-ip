package com.maksimowiczm.findmyip.data

import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    fun observeAddress(internetProtocolVersion: InternetProtocolVersion): Flow<Address>
    fun refreshAddresses()
}
