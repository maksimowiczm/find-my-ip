package com.maksimowiczm.findmyip.shared.core.domain

import kotlin.jvm.JvmInline

@OptIn(ExperimentalUnsignedTypes::class)
@JvmInline
value class Ip4Address(val array: UByteArray) : IpAddress {
    init {
        require(array.size == 4) {
            "IP address must be represented by an array of 4 bytes, but was ${array.size} bytes."
        }
    }

    override fun toString(): String =
        array.joinToString(separator = ".", transform = UByte::toString)

    override fun stringRepresentation(): String = toString()
}
