package com.maksimowiczm.findmyip.network

import java.net.URL
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class NetworkAddressDataSourceImpl(
    private val providerURL: String,
    private val connectivityObserver: ConnectivityObserver,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : NetworkAddressDataSource {
    private val scope = CoroutineScope(ioDispatcher + SupervisorJob())
    private val currentAddress = MutableStateFlow<Result<NetworkAddress?>>(Result.success(null))

    init {
        refreshAddress()
    }

    override fun observeAddress(): Flow<Result<NetworkAddress?>> = currentAddress

    override fun refreshAddress() {
        scope.launch {
            currentAddress.emit(Result.success(null))

            val networkType = connectivityObserver.observeNetworkType().firstOrNull()
            val result = runCatching {
                URL(providerURL).readText()
            }.map {
                NetworkAddress(
                    ip = it,
                    networkType = networkType
                )
            }

            currentAddress.emit(result)
        }
    }
}
