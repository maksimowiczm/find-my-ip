package com.maksimowiczm.findmyip.data.database

import androidx.room.TypeConverter
import com.maksimowiczm.findmyip.domain.model.InternetProtocol

@Suppress("unused")
class InternetProtocolConverter {
    @TypeConverter
    fun fromInternetProtocol(internetProtocol: InternetProtocol): Int = when (internetProtocol) {
        InternetProtocol.IPv4 -> 0
        InternetProtocol.IPv6 -> 1
    }

    @TypeConverter
    fun toInternetProtocol(internetProtocol: Int): InternetProtocol = when (internetProtocol) {
        0 -> InternetProtocol.IPv4
        1 -> InternetProtocol.IPv6
        else -> error("InternetProtocol not found")
    }
}
