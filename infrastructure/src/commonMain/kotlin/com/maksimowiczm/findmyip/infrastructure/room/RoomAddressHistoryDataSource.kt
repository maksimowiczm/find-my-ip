package com.maksimowiczm.findmyip.infrastructure.room

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.maksimowiczm.findmyip.application.infrastructure.local.AddressHistoryLocalDataSource
import com.maksimowiczm.findmyip.domain.entity.AddressHistory
import com.maksimowiczm.findmyip.domain.entity.NetworkType as DomainNetworkType
import com.maksimowiczm.findmyip.infrastructure.mapper.StringToAddressMapper
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

internal class RoomAddressHistoryDataSource(
    private val dao: AddressHistoryDao,
    private val stringToAddressMapper: StringToAddressMapper,
) : AddressHistoryLocalDataSource {
    override fun observeHistory(
        query: String?,
        ipv4: Boolean,
        ipv6: Boolean,
    ): Flow<PagingData<AddressHistory>> =
        Pager(
                config = PagingConfig(pageSize = 30),
                pagingSourceFactory = { dao.observePaged(query = query, ipv4 = ipv4, ipv6 = ipv6) },
            )
            .flow
            .map { data -> data.map { it.toModel() } }

    override suspend fun saveHistory(history: AddressHistory) {
        dao.insert(history.toEntity())
    }

    override suspend fun getLatestIp4Address(): AddressHistory? {
        val entity = dao.getLatestAddress(AddressVersion.IPV4)
        return entity?.toModel()
    }

    override suspend fun getLatestIp6Address(): AddressHistory? {
        val entity = dao.getLatestAddress(AddressVersion.IPV6)
        return entity?.toModel()
    }

    @OptIn(ExperimentalTime::class)
    private fun AddressHistoryEntity.toModel(): AddressHistory =
        when (addressVersion) {
            AddressVersion.IPV4 ->
                AddressHistory.Ipv4(
                    id = id,
                    address = stringToAddressMapper.toIp4Address(address),
                    domain = domain,
                    networkType = networkType(),
                    dateTime =
                        Instant.fromEpochSeconds(epochSeconds)
                            .toLocalDateTime(TimeZone.currentSystemDefault()),
                )

            AddressVersion.IPV6 ->
                AddressHistory.Ipv6(
                    id = id,
                    address = stringToAddressMapper.toIp6Address(address),
                    domain = domain,
                    networkType = networkType(),
                    dateTime =
                        Instant.fromEpochSeconds(epochSeconds)
                            .toLocalDateTime(TimeZone.currentSystemDefault()),
                )
        }

    private fun AddressHistoryEntity.networkType(): DomainNetworkType =
        when (this.networkType) {
            NetworkType.UNKNOWN -> DomainNetworkType.Unknown
            NetworkType.WIFI -> DomainNetworkType.WiFi
            NetworkType.CELLULAR -> DomainNetworkType.Cellular
            NetworkType.VPN -> DomainNetworkType.VPN
        }

    @OptIn(ExperimentalTime::class)
    private fun AddressHistory.toEntity(): AddressHistoryEntity {
        val version =
            when (this) {
                is AddressHistory.Ipv4 -> AddressVersion.IPV4
                is AddressHistory.Ipv6 -> AddressVersion.IPV6
            }

        val epochSeconds = dateTime.toInstant(TimeZone.currentSystemDefault()).epochSeconds

        return AddressHistoryEntity(
            id = id,
            address = stringRepresentation(),
            domain = domain,
            addressVersion = version,
            networkType = networkType.toEnum(),
            epochSeconds = epochSeconds,
        )
    }

    private fun DomainNetworkType.toEnum(): NetworkType =
        when (this) {
            DomainNetworkType.Unknown -> NetworkType.UNKNOWN
            DomainNetworkType.WiFi -> NetworkType.WIFI
            DomainNetworkType.Cellular -> NetworkType.CELLULAR
            DomainNetworkType.VPN -> NetworkType.VPN
        }
}
