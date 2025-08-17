package com.maksimowiczm.findmyip.domain.entity

import kotlin.jvm.JvmInline

@OptIn(ExperimentalUnsignedTypes::class)
@JvmInline
value class Ip6Address(val array: UByteArray) {
    init {
        require(array.size == 16) {
            "IP address must be represented by an array of 16 bytes, but was ${array.size} bytes."
        }
    }

    override fun toString(): String =
        array.joinToString(separator = ":", transform = UByte::toString)
}
