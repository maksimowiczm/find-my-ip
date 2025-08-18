package com.maksimowiczm.findmyip.domain.entity

import kotlinx.datetime.LocalDateTime

sealed interface AddressStatus<out T> {
    val date: LocalDateTime

    sealed interface Error<T> : AddressStatus<T> {

        data class Unknown<T>(override val date: LocalDateTime) : Error<T>

        data class Custom<T>(override val date: LocalDateTime, val message: String) : Error<T>
    }

    data class Success<T>(override val date: LocalDateTime, val value: T) : AddressStatus<T>
}
