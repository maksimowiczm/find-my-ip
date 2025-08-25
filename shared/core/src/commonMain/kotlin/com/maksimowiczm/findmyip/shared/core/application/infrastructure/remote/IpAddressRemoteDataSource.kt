package com.maksimowiczm.findmyip.shared.core.application.infrastructure.remote

import com.maksimowiczm.findmyip.shared.core.domain.IpAddress

fun interface IpAddressRemoteDataSource<A : IpAddress> {
    suspend fun getCurrentIpAddress(): A
}
