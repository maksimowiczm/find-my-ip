package com.maksimowiczm.findmyip.domain.entity

import kotlinx.datetime.LocalDateTime

sealed interface AddressHistory : IpAddress {
    val id: Long
    val domain: String?
    val dateTime: LocalDateTime

    data class Ipv4(
        override val id: Long,
        override val domain: String?,
        override val dateTime: LocalDateTime,
        val address: Ip4Address,
    ) : AddressHistory {
        override fun stringRepresentation(): String = address.stringRepresentation()
    }

    data class Ipv6(
        override val id: Long,
        override val domain: String?,
        override val dateTime: LocalDateTime,
        val address: Ip6Address,
    ) : AddressHistory {
        override fun stringRepresentation(): String = address.stringRepresentation()
    }
}
