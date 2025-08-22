package com.maksimowiczm.findmyip.infrastructure.fake

import com.maksimowiczm.findmyip.application.infrastructure.remote.Ip4AddressRemoteDataSource
import com.maksimowiczm.findmyip.application.infrastructure.remote.Ip6AddressRemoteDataSource
import com.maksimowiczm.findmyip.domain.entity.Ip4Address
import com.maksimowiczm.findmyip.domain.entity.Ip6Address
import com.maksimowiczm.findmyip.infrastructure.mapper.StringToAddressMapper
import kotlin.random.Random

internal class FakeAddressDataSource(
    private val random: Random,
    private val stringToAddressMapper: StringToAddressMapper,
) : Ip4AddressRemoteDataSource, Ip6AddressRemoteDataSource {
    override suspend fun getCurrentIp4Address(): Ip4Address {
        // 20% for failure
        if (random.nextInt(5) == 0) {
            error("Fake failure")
        }

        return CommonAddresses.v4.random(random).let(stringToAddressMapper::toIp4Address)
    }

    override suspend fun getCurrentIp6Address(): Ip6Address {
        // 20% for failure
        if (random.nextInt(5) == 0) {
            error("Fake failure")
        }

        return CommonAddresses.v6.random(random).let(stringToAddressMapper::toIp6Address)
    }
}
