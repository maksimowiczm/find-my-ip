package com.maksimowiczm.findmyip.domain.entity

sealed interface CurrentAddress {
    data class Ip4(val address: Ip4Address) : CurrentAddress

    data class Ip6(val address: Ip6Address) : CurrentAddress
}
