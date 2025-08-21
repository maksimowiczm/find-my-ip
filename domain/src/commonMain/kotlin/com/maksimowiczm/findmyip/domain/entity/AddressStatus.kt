package com.maksimowiczm.findmyip.domain.entity

import kotlinx.datetime.LocalDateTime

sealed interface AddressStatus<out T> {
    val dateTime: LocalDateTime

    sealed interface Error<T> : AddressStatus<T> {

        data class Unknown<T>(override val dateTime: LocalDateTime) : Error<T>

        data class Custom<T>(override val dateTime: LocalDateTime, val message: String) : Error<T>
    }

    data class Success<T>(override val dateTime: LocalDateTime, val value: T) : AddressStatus<T>
}
