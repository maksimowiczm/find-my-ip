package com.maksimowiczm.findmyip.domain.mapper

import com.maksimowiczm.findmyip.data.model.AddressEntity
import com.maksimowiczm.findmyip.domain.model.Address
import com.maksimowiczm.findmyip.domain.model.AddressId
import com.maksimowiczm.findmyip.domain.source.NetworkAddress
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

object AddressMapper {
    fun toDomain(
        addressEntity: AddressEntity,
        zone: TimeZone = TimeZone.currentSystemDefault()
    ): Address = addressEntity.toDomain(zone)

    fun toEntity(
        address: Address,
        zone: TimeZone = TimeZone.currentSystemDefault()
    ): AddressEntity = address.toEntity(zone)

    fun toDomain(address: NetworkAddress, id: AddressId): Address = address.toDomain(id)

    /**
     * Converts a [NetworkAddress] to an [AddressEntity]. Identifier is not set.
     */
    fun toEntity(
        address: NetworkAddress,
        zone: TimeZone = TimeZone.currentSystemDefault()
    ): AddressEntity = address.toEntity(zone)
}

private fun AddressEntity.toDomain(zone: TimeZone) = Address(
    id = AddressId(id),
    ip = ip,
    internetProtocol = internetProtocol,
    networkType = networkType,
    dateTime = Instant.fromEpochMilliseconds(epochMillis).toLocalDateTime(zone)
)

private fun Address.toEntity(zone: TimeZone) = AddressEntity(
    id = id.value,
    ip = ip,
    internetProtocol = internetProtocol,
    networkType = networkType,
    epochMillis = dateTime.toInstant(zone).toEpochMilliseconds()
)

private fun NetworkAddress.toDomain(id: AddressId): Address = Address(
    id = id,
    ip = ip,
    internetProtocol = internetProtocol,
    networkType = networkType,
    dateTime = dateTime
)

private fun NetworkAddress.toEntity(zone: TimeZone): AddressEntity = AddressEntity(
    ip = ip,
    internetProtocol = internetProtocol,
    networkType = networkType,
    epochMillis = dateTime.toInstant(zone).toEpochMilliseconds()
)
