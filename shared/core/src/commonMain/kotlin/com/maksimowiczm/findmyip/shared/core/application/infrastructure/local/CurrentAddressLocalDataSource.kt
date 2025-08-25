package com.maksimowiczm.findmyip.shared.core.application.infrastructure.local

import com.maksimowiczm.findmyip.shared.core.domain.AddressStatus
import com.maksimowiczm.findmyip.shared.core.domain.IpAddress
import kotlinx.coroutines.flow.Flow

interface CurrentAddressLocalDataSource<A : IpAddress> {
    fun observe(): Flow<AddressStatus<A>>

    suspend fun update(status: AddressStatus<A>)
}
