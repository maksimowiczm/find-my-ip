package com.maksimowiczm.findmyip.domain.source

import kotlinx.coroutines.flow.Flow

@JvmInline
value class Address(val ip: String)

sealed interface AddressState {
    data object Refreshing : AddressState
    data class Success(val address: Address) : AddressState
    data class Error(val error: Throwable?) : AddressState
}

interface AddressObserver {
    val flow: Flow<AddressState>

    suspend fun refresh(): Result<Address>
}
