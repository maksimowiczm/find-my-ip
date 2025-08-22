package com.maksimowiczm.findmyip.application.infrastructure.remote

import com.maksimowiczm.findmyip.domain.entity.IpAddress

fun interface IpAddressRemoteDataSource<A : IpAddress> {
    suspend fun getCurrentIpAddress(): A
}
