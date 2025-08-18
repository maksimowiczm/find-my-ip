package com.maksimowiczm.findmyip.domain.entity

import kotlinx.datetime.LocalDateTime

sealed interface AddressHistory {
    data class Ipv4(val address: Ip4Address, val dateTime: LocalDateTime) : AddressHistory

    data class Ipv6(val address: Ip6Address, val dateTime: LocalDateTime) : AddressHistory
}
