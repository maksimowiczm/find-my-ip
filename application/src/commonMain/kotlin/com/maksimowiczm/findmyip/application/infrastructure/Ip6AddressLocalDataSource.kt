package com.maksimowiczm.findmyip.application.infrastructure

import com.maksimowiczm.findmyip.domain.entity.Ip6Address
import kotlinx.coroutines.flow.Flow

interface Ip6AddressLocalDataSource {
    fun observeCurrentIp6Address(): Flow<Ip6Address>

    suspend fun saveCurrentIp6Address(address: Ip6Address)
}
