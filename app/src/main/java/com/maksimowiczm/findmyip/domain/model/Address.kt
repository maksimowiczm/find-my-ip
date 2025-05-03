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
