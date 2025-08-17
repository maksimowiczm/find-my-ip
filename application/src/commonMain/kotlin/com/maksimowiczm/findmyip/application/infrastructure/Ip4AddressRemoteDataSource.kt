package com.maksimowiczm.findmyip.application.infrastructure

import com.maksimowiczm.findmyip.domain.entity.Ip4Address

interface Ip4AddressRemoteDataSource {

    suspend fun getCurrentIp4Address(): Ip4Address
}
