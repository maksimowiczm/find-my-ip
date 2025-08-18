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

    override fun toString(): String {
        return array
            .asSequence()
            .chunked(2)
            .map { (msb, lsb) -> (msb.toInt() shl 8) or lsb.toInt() }
            .joinToString(":") { it.toString(16) }
    }

    fun stringRepresentation(): String = toString()
}
