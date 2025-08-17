package com.maksimowiczm.findmyip.infrastructure.mapper

import com.maksimowiczm.findmyip.domain.entity.Ip4Address

interface StringToAddressMapper {
    /**
     * Converts a string representation of an IP address to an [Ip4Address] object.
     *
     * @param ip The string representation of the IP address.
     * @return An [Ip4Address] object representing the given IP address.
     */
    fun toIp4Address(ip: String): Ip4Address
}

@OptIn(ExperimentalUnsignedTypes::class)
internal class StringToAddressMapperImpl : StringToAddressMapper {
    override fun toIp4Address(ip: String): Ip4Address {
        val parts = ip.split(".")
        require(parts.size == 4) { "Invalid IP address format: $ip" }
        val octets = parts.map { it.toUIntOrNull() ?: error("Invalid octet in IP address: $it") }
        require(octets.all { it <= 255u }) { "IP address octets must be in the range 0-255: $ip" }
        return Ip4Address(UByteArray(4) { index -> octets[index].toUByte() })
    }
}
