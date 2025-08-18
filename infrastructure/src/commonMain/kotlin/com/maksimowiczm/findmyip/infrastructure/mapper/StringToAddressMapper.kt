package com.maksimowiczm.findmyip.infrastructure.mapper

import com.maksimowiczm.findmyip.domain.entity.Ip4Address
import com.maksimowiczm.findmyip.domain.entity.Ip6Address

interface StringToAddressMapper {
    /**
     * Converts a string representation of an IP address to an [Ip4Address] object.
     *
     * @param ip The string representation of the IP address.
     * @return An [Ip4Address] object representing the given IP address.
     */
    fun toIp4Address(ip: String): Ip4Address

    /**
     * Converts a string representation of an IP address to an [Ip6Address] object.
     *
     * @param ip The string representation of the IP address.
     * @return An [Ip6Address] object representing the given IP address.
     */
    fun toIp6Address(ip: String): Ip6Address
}

@OptIn(ExperimentalUnsignedTypes::class)
internal object StringToAddressMapperImpl : StringToAddressMapper {
    override fun toIp4Address(ip: String): Ip4Address {
        val parts = ip.split(".")
        require(parts.size == 4) { "Invalid IP address format: $ip" }
        val octets = parts.map { it.toUIntOrNull() ?: error("Invalid octet in IP address: $it") }
        require(octets.all { it <= 255u }) { "IP address octets must be in the range 0-255: $ip" }
        return Ip4Address(UByteArray(4) { index -> octets[index].toUByte() })
    }

    override fun toIp6Address(ip: String): Ip6Address {
        val parts = ip.split(":")
        require(parts.size in 2..8) { "Invalid IP address format: $ip" }
        val octets =
            parts.flatMap { part ->
                if (part.isEmpty()) {
                    List(8 - parts.size + 1) { 0u } // Handle "::" shorthand
                } else {
                    val value =
                        part.toUIntOrNull() ?: error("Invalid hex value in IP address: $part")
                    require(value <= 0xFFFFu) { "Hex value must be in the range 0-FFFF: $part" }
                    listOf(value shr 8, value and 0xFFu)
                }
            }
        require(octets.size == 16) { "IP address must be represented by an array of 16 bytes: $ip" }
        return Ip6Address(UByteArray(16) { index -> octets[index].toUByte() })
    }
}
