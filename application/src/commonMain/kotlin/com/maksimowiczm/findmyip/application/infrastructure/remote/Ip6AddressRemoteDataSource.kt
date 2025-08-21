package com.maksimowiczm.findmyip.application.infrastructure.remote

import com.maksimowiczm.findmyip.domain.entity.Ip6Address

interface Ip6AddressRemoteDataSource {
    suspend fun getCurrentIp6Address(): Ip6Address
}
