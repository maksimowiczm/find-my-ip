package com.maksimowiczm.findmyip.infrastructure.room

import androidx.room.TypeConverter

internal enum class NetworkType {
    UNKNOWN,
    WIFI,
    CELLULAR,
    VPN,
}

internal class NetworkTypeTypeConverter {
    @TypeConverter
    fun fromNetworkType(networkType: NetworkType): String =
        when (networkType) {
            NetworkType.UNKNOWN -> NetworkTypeSQLConstants.UNKNOWN
            NetworkType.WIFI -> NetworkTypeSQLConstants.WIFI
            NetworkType.CELLULAR -> NetworkTypeSQLConstants.CELLULAR
            NetworkType.VPN -> NetworkTypeSQLConstants.VPN
        }

    @TypeConverter
    fun toNetworkType(value: String): NetworkType =
        when (value) {
            NetworkTypeSQLConstants.UNKNOWN -> NetworkType.UNKNOWN
            NetworkTypeSQLConstants.WIFI -> NetworkType.WIFI
            NetworkTypeSQLConstants.CELLULAR -> NetworkType.CELLULAR
            NetworkTypeSQLConstants.VPN -> NetworkType.VPN
            else -> error("Unknown NetworkType value: $value")
        }
}

internal object NetworkTypeSQLConstants {
    const val UNKNOWN = "unknown"
    const val WIFI = "wifi"
    const val CELLULAR = "cellular"
    const val VPN = "vpn"
}
