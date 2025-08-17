package com.maksimowiczm.findmyip.infrastructure.ipify

import com.maksimowiczm.findmyip.application.infrastructure.Ip4AddressRemoteDataSource
import com.maksimowiczm.findmyip.domain.entity.Ip4Address
import com.maksimowiczm.findmyip.infrastructure.mapper.StringToAddressMapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class IpifyAddressDataSource(
    config: IpifyConfig,
    private val httpClient: HttpClient,
    private val stringToAddressMapper: StringToAddressMapper,
) : Ip4AddressRemoteDataSource {

    private val ip4Url = config.ip4Url

    override suspend fun getCurrentIp4Address(): Ip4Address =
        httpClient.get(ip4Url).body<String>().let(stringToAddressMapper::toIp4Address)
}
