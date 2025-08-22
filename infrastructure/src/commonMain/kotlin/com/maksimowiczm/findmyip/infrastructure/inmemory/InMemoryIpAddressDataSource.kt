package com.maksimowiczm.findmyip.infrastructure.inmemory

import com.maksimowiczm.findmyip.application.infrastructure.local.CurrentAddressLocalDataSource
import com.maksimowiczm.findmyip.domain.entity.AddressStatus
import com.maksimowiczm.findmyip.domain.entity.IpAddress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

internal class InMemoryIpAddressDataSource<A : IpAddress> : CurrentAddressLocalDataSource<A> {
    private val ip = MutableStateFlow<AddressStatus<A>?>(null)

    override fun observe(): Flow<AddressStatus<A>> = ip.filterNotNull()

    override suspend fun update(status: AddressStatus<A>) {
        ip.value = status
    }
}
