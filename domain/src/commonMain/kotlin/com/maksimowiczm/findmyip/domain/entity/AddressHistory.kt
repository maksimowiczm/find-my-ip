package com.maksimowiczm.findmyip.domain.entity

import kotlinx.datetime.LocalDateTime

sealed interface AddressHistory {
    val id: Long
    val dateTime: LocalDateTime

    data class Ipv4(
        override val id: Long,
        val address: Ip4Address,
        override val dateTime: LocalDateTime,
    ) : AddressHistory

    data class Ipv6(
        override val id: Long,
        val address: Ip6Address,
        override val dateTime: LocalDateTime,
    ) : AddressHistory
}
