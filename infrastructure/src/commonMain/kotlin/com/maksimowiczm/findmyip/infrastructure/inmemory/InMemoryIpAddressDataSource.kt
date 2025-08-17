package com.maksimowiczm.findmyip.infrastructure.inmemory

import com.maksimowiczm.findmyip.application.infrastructure.Ip4AddressLocalDataSource
import com.maksimowiczm.findmyip.domain.entity.Ip4Address
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

internal class InMemoryIpAddressDataSource : Ip4AddressLocalDataSource {
    private val ip4 = MutableStateFlow<Ip4Address?>(null)

    override fun observeCurrentIp4Address(): Flow<Ip4Address> = ip4.filterNotNull()

    override suspend fun saveCurrentIp4Address(address: Ip4Address) {
        ip4.emit(address)
    }
}
