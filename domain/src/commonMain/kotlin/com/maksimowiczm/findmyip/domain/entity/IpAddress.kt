package com.maksimowiczm.findmyip.domain.entity

sealed interface IpAddress : IpAddressString

interface IpAddressString {
    fun stringRepresentation(): String
}
