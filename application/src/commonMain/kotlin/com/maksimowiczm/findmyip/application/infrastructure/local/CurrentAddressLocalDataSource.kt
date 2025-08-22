package com.maksimowiczm.findmyip.application.infrastructure.local

import com.maksimowiczm.findmyip.domain.entity.AddressStatus
import com.maksimowiczm.findmyip.domain.entity.IpAddress
import kotlinx.coroutines.flow.Flow

interface CurrentAddressLocalDataSource<A : IpAddress> {
    fun observe(): Flow<AddressStatus<A>>

    suspend fun update(status: AddressStatus<A>)
}
