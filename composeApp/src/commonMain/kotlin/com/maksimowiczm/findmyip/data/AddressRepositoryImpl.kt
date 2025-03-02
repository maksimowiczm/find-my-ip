package com.maksimowiczm.findmyip.data

import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.network.NetworkAddressDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AddressRepositoryImpl(
    private val ipv4DataSource: NetworkAddressDataSource,
    private val ipv6DataSource: NetworkAddressDataSource
) : AddressRepository {
    override fun observeAddress(
        internetProtocolVersion: InternetProtocolVersion
    ): Flow<AddressStatus> {
        val flow = when (internetProtocolVersion) {
            InternetProtocolVersion.IPv4 -> ipv4DataSource.observeAddress()
            InternetProtocolVersion.IPv6 -> ipv6DataSource.observeAddress()
        }

        return flow
            .map { result ->
                if (result.isFailure) {
                    return@map AddressStatus.Error(result.exceptionOrNull())
                }

                val address = result.getOrNull()

                if (address != null) {
                    AddressStatus.Success(
                        Address(
                            ip = address,
                            protocolVersion = internetProtocolVersion
                        )
                    )
                } else {
                    AddressStatus.Loading
                }
            }
    }

    override fun refreshAddresses() {
        ipv4DataSource.refreshAddress()
        ipv6DataSource.refreshAddress()
    }
}
