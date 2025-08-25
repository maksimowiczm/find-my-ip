package com.maksimowiczm.findmyip.shared.core.infrastructure.mapper

import com.maksimowiczm.findmyip.shared.core.domain.Ip4Address
import com.maksimowiczm.findmyip.shared.core.domain.Ip6Address

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
        val trimmedIp = ip.trim()

        // Handle IPv4-mapped IPv6 addresses (e.g., ::ffff:192.0.2.1)
        val ipv4Pattern = Regex("""^(.*):((?:\d{1,3}\.){3}\d{1,3})$""")
        val ipv4Match = ipv4Pattern.find(trimmedIp)

        val actualIp =
            if (ipv4Match != null) {
                val (prefix, ipv4) = ipv4Match.destructured
                val ipv4Parts = ipv4.split(".")
                if (
                    ipv4Parts.size != 4 ||
                        ipv4Parts.any { it.toIntOrNull()?.let { num -> num !in 0..255 } != false }
                ) {
                    error("Invalid IPv4 part in IPv6 address: $ipv4")
                }

                val hex1 =
                    (ipv4Parts[0].toInt() shl 8 or ipv4Parts[1].toInt())
                        .toString(16)
                        .padStart(4, '0')
                val hex2 =
                    (ipv4Parts[2].toInt() shl 8 or ipv4Parts[3].toInt())
                        .toString(16)
                        .padStart(4, '0')
                "$prefix:$hex1:$hex2"
            } else {
                trimmedIp
            }

        // Split by "::" to handle compression
        val parts =
            if (actualIp.contains("::")) {
                val split = actualIp.split("::", limit = 2)
                if (split.size > 2) {
                    error("Invalid IPv6 address: multiple :: found")
                }

                val leftParts = if (split[0].isEmpty()) emptyList() else split[0].split(":")
                val rightParts =
                    if (split.size == 1 || split[1].isEmpty()) emptyList() else split[1].split(":")

                val missingGroups = 8 - leftParts.size - rightParts.size
                if (missingGroups < 0) {
                    error("Invalid IPv6 address: too many groups")
                }

                leftParts + List(missingGroups) { "0" } + rightParts
            } else {
                val groups = actualIp.split(":")
                if (groups.size != 8) {
                    error("Invalid IPv6 address: must have 8 groups or use :: notation")
                }
                groups
            }

        if (parts.size != 8) {
            error("Invalid IPv6 address: incorrect number of groups")
        }

        // Convert each group to bytes
        val bytes = UByteArray(16)
        var byteIndex = 0

        for (group in parts) {
            if (group.isEmpty()) {
                error("Invalid IPv6 address: empty group")
            }

            // Validate hex group (1-4 hex digits)
            if (!group.matches(Regex("^[0-9a-fA-F]{1,4}$"))) {
                error("Invalid IPv6 address: invalid hex group '$group'")
            }

            val value = group.toInt(16)
            bytes[byteIndex++] = ((value shr 8) and 0xFF).toUByte()
            bytes[byteIndex++] = (value and 0xFF).toUByte()
        }

        return Ip6Address(bytes)
    }
}
