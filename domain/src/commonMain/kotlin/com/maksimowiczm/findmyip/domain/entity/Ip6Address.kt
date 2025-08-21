package com.maksimowiczm.findmyip.domain.entity

import kotlin.jvm.JvmInline

@OptIn(ExperimentalUnsignedTypes::class)
@JvmInline
value class Ip6Address(val array: UByteArray) : IpAddress {
    init {
        require(array.size == 16) {
            "IP address must be represented by an array of 16 bytes, but was ${array.size} bytes."
        }
    }

    override fun toString(): String {
        // Convert bytes to 16-bit groups (hextets)
        val groups = mutableListOf<UInt>()
        for (i in 0 until 16 step 2) {
            val high = array[i].toUInt()
            val low = array[i + 1].toUInt()

            groups.add(((high shl 8) or low) % 0x10000U)
        }

        // Find the longest sequence of consecutive zeros for compression
        var longestZeroStart = -1
        var longestZeroLength = 0
        var currentZeroStart = -1
        var currentZeroLength = 0

        for (i in groups.indices) {
            if (groups[i].toUShort() == 0.toUShort()) {
                if (currentZeroStart == -1) {
                    currentZeroStart = i
                    currentZeroLength = 1
                } else {
                    currentZeroLength++
                }
            } else {
                if (currentZeroLength > longestZeroLength) {
                    longestZeroStart = currentZeroStart
                    longestZeroLength = currentZeroLength
                }
                currentZeroStart = -1
                currentZeroLength = 0
            }
        }

        // Check if the last sequence was the longest
        if (currentZeroLength > longestZeroLength) {
            longestZeroStart = currentZeroStart
            longestZeroLength = currentZeroLength
        }

        // Only compress if we have 2 or more consecutive zeros
        val shouldCompress = longestZeroLength >= 2

        val result = StringBuilder()
        var i = 0
        var compressed = false

        while (i < groups.size) {
            if (shouldCompress && i == longestZeroStart && !compressed) {
                // Add "::" for compression
                if (i == 0) {
                    result.append("::")
                } else {
                    result.append("::")
                }
                compressed = true
                i += longestZeroLength
            } else {
                // Add the hextet, removing leading zeros
                val hexValue = groups[i].toString(16)
                result.append(hexValue)

                // Add colon separator if not the last group
                if (i < groups.size - 1 && !(shouldCompress && i + 1 == longestZeroStart)) {
                    result.append(":")
                }
                i++
            }
        }

        // Handle edge case where compression is at the end
        if (shouldCompress && longestZeroStart + longestZeroLength == groups.size && compressed) {
            if (!result.endsWith("::")) {
                result.append(":")
            }
        }

        return result.toString()
    }

    override fun stringRepresentation(): String = toString()
}
