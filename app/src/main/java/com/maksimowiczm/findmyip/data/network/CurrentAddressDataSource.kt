package com.maksimowiczm.findmyip.data.network

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.getOrElse
import com.github.michaelbull.result.map
import com.github.michaelbull.result.runCatching
import com.maksimowiczm.findmyip.data.model.Address
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
    data object NetworkUnavailable : CurrentAddressState
    data class Error(val error: Throwable) : CurrentAddressState
}

class CurrentAddressDataSource(
    private val networkDispatcher: CoroutineDispatcher,
    private val connectivityObserver: ConnectivityObserver
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

    suspend fun refreshCurrentAddress(): Result<Address, Unit> {
        return withContext(networkDispatcher) {
            currentAddress.emit(CurrentAddressState.Loading)

            val networkType = connectivityObserver.getNetworkType()

            if (networkType == null) {
                currentAddress.emit(CurrentAddressState.NetworkUnavailable)
                return@withContext Err(Unit)
            }

            // theoretically network request could be executed on mobile data, it a race condition
            return@withContext runCatching {
                URL("https://api.ipify.org").readText()
            }.map {
                val address = Address(
                    ip = it,
                    date = Calendar.getInstance().time,
                    networkType = networkType
                )
                currentAddress.emit(CurrentAddressState.Success(address))
                Ok(address)
            }.getOrElse {
                currentAddress.emit(CurrentAddressState.Error(it))
                Err(Unit)
            }
        }
    }
}
