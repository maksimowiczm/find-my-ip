package com.maksimowiczm.findmyip.old.network

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.getOrElse
import com.github.michaelbull.result.map
import com.github.michaelbull.result.runCatching
import com.maksimowiczm.findmyip.old.data.model.Address
import com.maksimowiczm.findmyip.old.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.old.network.CurrentAddressState
import java.net.URL
import java.util.Calendar
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

actual class CurrentAddressDataSource(
    private val networkDispatcher: CoroutineDispatcher,
    private val connectivityObserver: ConnectivityObserver,
    private val addressProviderUrl: String,
    private val internetProtocolVersion: InternetProtocolVersion
) {
    private val currentAddress = MutableStateFlow<CurrentAddressState>(CurrentAddressState.None)

    actual fun observeCurrentAddress(autoFetch: Boolean): Flow<CurrentAddressState> {
        if (autoFetch && currentAddress.value is CurrentAddressState.None) {
            CoroutineScope(networkDispatcher).launch {
                refreshCurrentAddress()
            }
        }

        return currentAddress
    }

    actual suspend fun refreshCurrentAddress(): Result<Address, Unit> {
        return withContext(networkDispatcher) {
            currentAddress.emit(CurrentAddressState.Loading)

            val networkType = connectivityObserver.getNetworkType()

            if (networkType == null) {
                currentAddress.emit(CurrentAddressState.NetworkUnavailable)
                return@withContext Err(Unit)
            }

            // theoretically network request could be executed on mobile data, it a race condition
            return@withContext runCatching {
                URL(addressProviderUrl).readText()
            }.map {
                val address = Address(
                    ip = it,
                    date = Calendar.getInstance().time,
                    networkType = networkType,
                    internetProtocolVersion = internetProtocolVersion
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
