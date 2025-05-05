package com.maksimowiczm.findmyip.domain.model

sealed interface NetworkType {
    data object WiFi : NetworkType
    data object Cellular : NetworkType
    data object VPN : NetworkType
}
