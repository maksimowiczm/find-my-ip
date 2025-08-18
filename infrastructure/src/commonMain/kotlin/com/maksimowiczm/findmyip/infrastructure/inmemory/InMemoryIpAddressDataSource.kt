package com.maksimowiczm.findmyip.infrastructure.inmemory

import com.maksimowiczm.findmyip.application.infrastructure.Ip4AddressLocalDataSource
import com.maksimowiczm.findmyip.application.infrastructure.Ip6AddressLocalDataSource
import com.maksimowiczm.findmyip.domain.entity.Ip4Address
import com.maksimowiczm.findmyip.domain.entity.Ip6Address
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

internal class InMemoryIpAddressDataSource : Ip4AddressLocalDataSource, Ip6AddressLocalDataSource {
    private val ip4 = MutableStateFlow<Ip4Address?>(null)
    private val ip6 = MutableStateFlow<Ip6Address?>(null)

    override fun observeCurrentIp4Address(): Flow<Ip4Address> = ip4.filterNotNull()

    override suspend fun saveCurrentIp4Address(address: Ip4Address) {
        ip4.emit(address)
    }

    override fun observeCurrentIp6Address(): Flow<Ip6Address> = ip6.filterNotNull()

    override suspend fun saveCurrentIp6Address(address: Ip6Address) {
        ip6.emit(address)
    }
}
