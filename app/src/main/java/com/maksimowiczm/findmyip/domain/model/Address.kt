package com.maksimowiczm.findmyip.domain.model

import kotlinx.datetime.LocalDateTime

@JvmInline
value class AddressId(val value: Long)

data class Address(
    val id: AddressId,
    val ip: String,
    val internetProtocol: InternetProtocol,
    val networkType: NetworkType,
    val dateTime: LocalDateTime
)

fun testAddress(
    id: AddressId = AddressId(0),
    ip: String = "127.0.0.1",
    internetProtocol: InternetProtocol = InternetProtocol.IPv4,
    networkType: NetworkType = NetworkType.WiFi,
    dateTime: LocalDateTime = LocalDateTime(2025, 5, 3, 10, 57)
): Address = Address(
    id = id,
    ip = ip,
    internetProtocol = internetProtocol,
    networkType = networkType,
    dateTime = dateTime
)
