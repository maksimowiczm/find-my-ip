package com.maksimowiczm.findmyip.application.infrastructure.local

import com.maksimowiczm.findmyip.domain.entity.AddressStatus
import com.maksimowiczm.findmyip.domain.entity.Ip4Address
import kotlinx.coroutines.flow.Flow

interface CurrentIp4AddressLocalDataSource {
    fun observeIp4(): Flow<AddressStatus<Ip4Address>>

    suspend fun updateIp4(status: AddressStatus<Ip4Address>)
}
