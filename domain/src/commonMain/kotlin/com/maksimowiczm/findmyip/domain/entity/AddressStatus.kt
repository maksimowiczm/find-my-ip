package com.maksimowiczm.findmyip.domain.entity

sealed interface AddressStatus<T> {

    sealed interface Error<T> : AddressStatus<T> {

        class Unknown<T> : Error<T>

        data class Custom<T>(val message: String) : Error<T>
    }

    data class Success<T>(val value: T) : AddressStatus<T>
}
