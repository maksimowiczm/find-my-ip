package com.maksimowiczm.findmyip.shared.feature.home.persentation

import com.maksimowiczm.findmyip.shared.core.domain.NetworkType as DomainNetworkType

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
