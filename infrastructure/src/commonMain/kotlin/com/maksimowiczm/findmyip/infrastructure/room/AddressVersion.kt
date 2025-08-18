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
            AddressVersion.IPV4 -> 4
            AddressVersion.IPV6 -> 6
        }

    @TypeConverter
    fun toAddressVersion(value: Int): AddressVersion =
        when (value) {
            4 -> AddressVersion.IPV4
            6 -> AddressVersion.IPV6
            else -> error("Unknown AddressVersion value: $value")
        }
}
