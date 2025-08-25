package com.maksimowiczm.findmyip.shared.core.domain

sealed interface NetworkType {
    data object Unknown : NetworkType

    data object WiFi : NetworkType

    data object Cellular : NetworkType

    data object VPN : NetworkType
}
