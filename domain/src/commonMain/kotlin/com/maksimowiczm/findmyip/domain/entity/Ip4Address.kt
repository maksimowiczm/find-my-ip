package com.maksimowiczm.findmyip.domain.entity

import kotlin.jvm.JvmInline

@OptIn(ExperimentalUnsignedTypes::class)
@JvmInline
value class Ip4Address(val array: UByteArray) {
    init {
        require(array.size == 4) {
            "IP address must be represented by an array of 4 bytes, but was ${array.size} bytes."
        }
    }

    override fun toString(): String =
        array.joinToString(separator = ".", transform = UByte::toString)
}
