package com.maksimowiczm.findmyip.application.infrastructure.local

import com.maksimowiczm.findmyip.domain.entity.AddressStatus
import com.maksimowiczm.findmyip.domain.entity.Ip6Address
import kotlinx.coroutines.flow.Flow

interface CurrentIp6AddressLocalDataSource {
    fun observeIp6(): Flow<AddressStatus<Ip6Address>>

    suspend fun updateIp6(status: AddressStatus<Ip6Address>)
}
