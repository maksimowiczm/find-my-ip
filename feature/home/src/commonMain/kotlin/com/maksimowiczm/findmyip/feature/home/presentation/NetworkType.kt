package com.maksimowiczm.findmyip.feature.home.presentation

import com.maksimowiczm.findmyip.domain.entity.NetworkType as DomainNetworkType

internal enum class NetworkType {
    UNKNOWN,
    WIFI,
    CELLULAR,
    VPN;

    companion object {
        fun fromDomain(domain: DomainNetworkType): NetworkType =
            when (domain) {
                DomainNetworkType.Unknown -> UNKNOWN
                DomainNetworkType.WiFi -> WIFI
                DomainNetworkType.Cellular -> CELLULAR
                DomainNetworkType.VPN -> VPN
            }
    }
}
