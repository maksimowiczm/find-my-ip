package com.maksimowiczm.findmyip.domain.repository

import com.maksimowiczm.findmyip.domain.mapper.AddressMapper
import com.maksimowiczm.findmyip.domain.model.Address
import com.maksimowiczm.findmyip.domain.model.AddressId
import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.model.NetworkType
import com.maksimowiczm.findmyip.domain.source.AddressLocalDataSource
import com.maksimowiczm.findmyip.ext.filterValues
import com.maksimowiczm.findmyip.ext.mapValues
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

interface AddressRepository {
    fun observeAddresses(
        query: String,
        internetProtocolFilters: List<InternetProtocol>,
        networkTypeFilters: List<NetworkType>,
        startDate: LocalDate?,
        endDate: LocalDate?
    ): Flow<List<Address>>

    suspend fun deleteAddress(id: AddressId)
}

class AddressRepositoryImpl(
    private val localDataSource: AddressLocalDataSource,
    private val addressMapper: AddressMapper
) : AddressRepository {
    @OptIn(ExperimentalTime::class)
    override fun observeAddresses(
        query: String,
        internetProtocolFilters: List<InternetProtocol>,
        networkTypeFilters: List<NetworkType>,
        startDate: LocalDate?,
        endDate: LocalDate?
    ): Flow<List<Address>> = localDataSource
        .observeAddresses(
            query = query,
            start = startDate
                ?.atStartOfDayIn(TimeZone.currentSystemDefault())
                ?.toEpochMilliseconds(),
            end = endDate
                ?.atStartOfDayIn(TimeZone.currentSystemDefault())
                ?.toEpochMilliseconds()
        )
        .filterValues {
            (
                internetProtocolFilters.isEmpty() ||
                    internetProtocolFilters.contains(it.internetProtocol)
                ) &&
                (networkTypeFilters.isEmpty() || networkTypeFilters.contains(it.networkType))
        }
        .mapValues { addressMapper.toDomain(it) }

    override suspend fun deleteAddress(id: AddressId) {
        val address = localDataSource.getAddress(id.value) ?: return
        localDataSource.deleteAddress(address)
    }
}
