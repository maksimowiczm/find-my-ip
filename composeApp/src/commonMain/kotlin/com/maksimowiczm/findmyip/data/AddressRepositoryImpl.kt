package com.maksimowiczm.findmyip.data

import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.network.AddressStatus
import com.maksimowiczm.findmyip.network.NetworkAddressDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AddressRepositoryImpl(
    private val ipv4source: NetworkAddressDataSource,
    private val ipv6source: NetworkAddressDataSource
) : AddressRepository {
    override fun observeAddress(internetProtocolVersion: InternetProtocolVersion): Flow<Address> =
        when (internetProtocolVersion) {
            InternetProtocolVersion.IPv4 -> ipv4source.addressFlow.map { it.toAddress() }
            InternetProtocolVersion.IPv6 -> ipv6source.addressFlow.map { it.toAddress() }
        }

    override fun refreshAddresses() {
        ipv4source.refreshAddress()
        ipv6source.refreshAddress()
    }
}

private fun AddressStatus.toAddress(): Address = when (this) {
    AddressStatus.None,
    AddressStatus.InProgress -> Address.Loading

    is AddressStatus.Success -> Address.Success(address.ip)
    is AddressStatus.Error -> Address.Error(exception.message ?: "Unknown error")
}
