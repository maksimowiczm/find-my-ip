package com.maksimowiczm.findmyip.domain.usecase

import com.maksimowiczm.findmyip.domain.mapper.AddressMapper
import com.maksimowiczm.findmyip.domain.model.Address
import com.maksimowiczm.findmyip.domain.source.AddressLocalDataSource
import com.maksimowiczm.findmyip.domain.source.NetworkAddress

fun interface InsertNetworkAddressIfChangedUseCase {
    /**
     * Inserts the network address into the local data source if it is different from the last one.
     *
     * @param networkAddress The network address to insert.
     * @return The inserted address, or null if it was not inserted.
     */
    suspend fun insertNetworkAddressIfChanged(networkAddress: NetworkAddress): Address?
}

class InsertNetworkAddressIfChangedUseCaseImpl(
    private val addressLocalDataSource: AddressLocalDataSource,
    private val addressMapper: AddressMapper
) : InsertNetworkAddressIfChangedUseCase {
    override suspend fun insertNetworkAddressIfChanged(networkAddress: NetworkAddress): Address? =
        addressLocalDataSource
            .insertAddressIfUniqueToLast(addressMapper.toEntity(networkAddress))
            ?.let { addressMapper.toDomain(it) }
}
