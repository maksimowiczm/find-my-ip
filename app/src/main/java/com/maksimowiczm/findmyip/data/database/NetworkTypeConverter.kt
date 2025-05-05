package com.maksimowiczm.findmyip.data.database

import androidx.room.TypeConverter
import com.maksimowiczm.findmyip.domain.model.NetworkType

@Suppress("unused")
class NetworkTypeConverter {
    @TypeConverter
    fun fromNetworkType(networkType: NetworkType): String = when (networkType) {
        NetworkType.Cellular -> "cellular"
        NetworkType.VPN -> "vpn"
        NetworkType.WiFi -> "wifi"
    }

    @TypeConverter
    fun toNetworkType(networkType: String): NetworkType = when (networkType) {
        "cellular" -> NetworkType.Cellular
        "vpn" -> NetworkType.VPN
        "wifi" -> NetworkType.WiFi
        else -> error("NetworkType not found")
    }
}
