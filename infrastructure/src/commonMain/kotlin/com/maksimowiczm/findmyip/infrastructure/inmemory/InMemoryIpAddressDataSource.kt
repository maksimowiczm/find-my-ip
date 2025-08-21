package com.maksimowiczm.findmyip.infrastructure.inmemory

import com.maksimowiczm.findmyip.application.infrastructure.local.CurrentIp4AddressLocalDataSource
import com.maksimowiczm.findmyip.application.infrastructure.local.CurrentIp6AddressLocalDataSource
import com.maksimowiczm.findmyip.domain.entity.AddressStatus
import com.maksimowiczm.findmyip.domain.entity.Ip4Address
import com.maksimowiczm.findmyip.domain.entity.Ip6Address
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

internal class InMemoryIpAddressDataSource :
    CurrentIp4AddressLocalDataSource, CurrentIp6AddressLocalDataSource {
    private val ip4 = MutableStateFlow<AddressStatus<Ip4Address>?>(null)

    override fun observeIp4(): Flow<AddressStatus<Ip4Address>> = ip4.filterNotNull()

    override suspend fun updateIp4(status: AddressStatus<Ip4Address>) {
        ip4.emit(status)
    }

    private val ip6 = MutableStateFlow<AddressStatus<Ip6Address>?>(null)

    override fun observeIp6(): Flow<AddressStatus<Ip6Address>> = ip6.filterNotNull()

    override suspend fun updateIp6(status: AddressStatus<Ip6Address>) {
        ip6.emit(status)
    }
}
