package com.maksimowiczm.findmyip.domain.entity

import kotlinx.datetime.LocalDateTime

sealed interface AddressStatus<out T : IpAddress> {
    val dateTime: LocalDateTime

    sealed interface Error<out T : IpAddress> : AddressStatus<T> {

        data class Unknown<out T : IpAddress>(override val dateTime: LocalDateTime) : Error<T>

        data class Custom<out T : IpAddress>(
            override val dateTime: LocalDateTime,
            val message: String,
        ) : Error<T>
    }

    data class Success<out T : IpAddress>(
        override val dateTime: LocalDateTime,
        val address: T,
        val domain: String?,
        val networkType: NetworkType,
    ) : AddressStatus<T>
}
