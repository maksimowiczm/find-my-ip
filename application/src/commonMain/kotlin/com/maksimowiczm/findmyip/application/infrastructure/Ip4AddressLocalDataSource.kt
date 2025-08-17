package com.maksimowiczm.findmyip.application.infrastructure

import com.maksimowiczm.findmyip.domain.entity.Ip4Address
import kotlinx.coroutines.flow.Flow

interface Ip4AddressLocalDataSource {

    fun observeCurrentIp4Address(): Flow<Ip4Address>

    suspend fun saveCurrentIp4Address(address: Ip4Address)
}
