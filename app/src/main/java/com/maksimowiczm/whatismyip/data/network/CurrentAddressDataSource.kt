package com.maksimowiczm.whatismyip.data.network

import com.github.michaelbull.result.getOrElse
import com.github.michaelbull.result.map
import com.github.michaelbull.result.runCatching
import com.maksimowiczm.whatismyip.data.model.Address
import java.net.URL
import java.util.Calendar
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface CurrentAddressState {
    object None : CurrentAddressState
    object Loading : CurrentAddressState
    data class Success(val address: Address) : CurrentAddressState
    data class Error(val error: Throwable) : CurrentAddressState
}

class CurrentAddressDataSource(
    private val networkDispatcher: CoroutineDispatcher
) {
    private val currentAddress = MutableStateFlow<CurrentAddressState>(CurrentAddressState.None)

    fun observeCurrentAddress(autoFetch: Boolean): Flow<CurrentAddressState> {
        if (autoFetch && currentAddress.value is CurrentAddressState.None) {
            CoroutineScope(networkDispatcher).launch {
                refreshCurrentAddress()
            }
        }

        return currentAddress
    }

    suspend fun refreshCurrentAddress(): CurrentAddressState {
        return withContext(networkDispatcher) {
            currentAddress.emit(CurrentAddressState.Loading)

            val result = runCatching {
                URL("https://api.ipify.org").readText()
            }.map {
                CurrentAddressState.Success(
                    Address(
                        ip = it,
                        date = Calendar.getInstance().time
                    )
                )
            }.getOrElse {
                CurrentAddressState.Error(it)
            }

            currentAddress.emit(result)
            result
        }
    }
}
