package com.maksimowiczm.findmyip.shared.core.domain

sealed interface IpAddress : IpAddressString

interface IpAddressString {
    fun stringRepresentation(): String
}
