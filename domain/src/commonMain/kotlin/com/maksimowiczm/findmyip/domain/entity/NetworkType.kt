package com.maksimowiczm.findmyip.domain.entity

sealed interface NetworkType {
    data object Unknown : NetworkType

    data object WiFi : NetworkType

    data object Cellular : NetworkType

    data object VPN : NetworkType
}
