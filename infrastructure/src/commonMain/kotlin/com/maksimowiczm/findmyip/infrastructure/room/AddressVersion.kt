package com.maksimowiczm.findmyip.infrastructure.room

import androidx.room.TypeConverter

internal enum class AddressVersion {
    IPV4,
    IPV6,
}

internal class AddressVersionTypeConverter {
    @TypeConverter
    fun fromAddressVersion(addressVersion: AddressVersion): Int =
        when (addressVersion) {
            AddressVersion.IPV4 -> AddressVersionSQLConstants.IPV4
            AddressVersion.IPV6 -> AddressVersionSQLConstants.IPV6
        }

    @TypeConverter
    fun toAddressVersion(value: Int): AddressVersion =
        when (value) {
            AddressVersionSQLConstants.IPV4 -> AddressVersion.IPV4
            AddressVersionSQLConstants.IPV6 -> AddressVersion.IPV6
            else -> error("Unknown AddressVersion value: $value")
        }
}

internal object AddressVersionSQLConstants {
    const val IPV4 = 4
    const val IPV6 = 6
}
