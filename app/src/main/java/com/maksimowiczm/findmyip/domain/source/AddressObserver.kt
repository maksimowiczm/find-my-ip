package com.maksimowiczm.findmyip.domain.source

import com.maksimowiczm.findmyip.data.model.AddressEntity
import kotlinx.coroutines.flow.Flow

sealed interface AddressState {
    data object Refreshing : AddressState
    data class Success(val address: AddressEntity) : AddressState
    data class Error(val error: Throwable?) : AddressState
}

interface AddressObserver {
    val flow: Flow<AddressState>

    suspend fun refresh(): Result<AddressEntity>
}
