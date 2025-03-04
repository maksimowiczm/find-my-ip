package com.maksimowiczm.findmyip.network

import java.net.URL
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NetworkAddressDataSourceImpl(
    private val providerURL: String,
    private val connectivityObserver: ConnectivityObserver,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : NetworkAddressDataSource {
    private val scope = CoroutineScope(ioDispatcher)
    private val currentAddress = MutableStateFlow<Result<NetworkAddress?>>(Result.success(null))

    init {
        scope.launch {
            refreshAddress()
        }
    }

    override fun observeAddress(): Flow<Result<NetworkAddress?>> = currentAddress

    override suspend fun refreshAddress(): Result<NetworkAddress> = withContext(ioDispatcher) {
        currentAddress.emit(Result.success(null))

        val networkType = connectivityObserver.getNetworkType()

        if (networkType == null) {
            return@withContext Result.failure(Exception("Network unavailable"))
        }

        val result = runCatching {
            URL(providerURL).readText()
        }.map {
            NetworkAddress(
                ip = it,
                networkType = networkType
            )
        }

        currentAddress.emit(result)

        result
    }
}
