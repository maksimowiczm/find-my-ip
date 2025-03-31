@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.data.model

import com.maksimowiczm.findmyip.database.AddressEntity
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

data class Address(
    val ip: String,
    val protocolVersion: InternetProtocolVersion,
    val date: LocalDateTime,
    val networkType: NetworkType?
)

fun AddressEntity.toDomain(): Address {
    val date =
        Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(TimeZone.currentSystemDefault())

    return Address(
        ip = ip,
        protocolVersion = internetProtocolVersion,
        date = date,
        networkType = null
    )
}

fun Address.toEntity(): AddressEntity = AddressEntity(
    ip = ip,
    internetProtocolVersion = protocolVersion,
    timestamp = date.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
)
