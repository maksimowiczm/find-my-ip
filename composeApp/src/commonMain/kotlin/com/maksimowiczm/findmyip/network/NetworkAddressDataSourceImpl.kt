package com.maksimowiczm.findmyip.network

import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NetworkAddressDataSourceImpl(
    override val providerURL: String,
    private val connectivityObserver: ConnectivityObserver,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : NetworkAddressDataSource {
    private val scope = CoroutineScope(ioDispatcher)
    private val currentAddress = MutableStateFlow<AddressStatus>(AddressStatus.Loading)

    init {
        scope.launch {
            refreshAddress()
        }
    }

    override fun observeAddress(): Flow<AddressStatus> = currentAddress

    override suspend fun refreshAddress(): Result<NetworkAddress> = withContext(ioDispatcher) {
        currentAddress.emit(AddressStatus.Loading)

        val networkType = connectivityObserver.getNetworkType()

        return@withContext try {
            val ip = URL(providerURL).readText()
            val address = NetworkAddress(
                ip = ip,
                networkType = networkType
            )

            currentAddress.emit(AddressStatus.Success(address))
            Result.success(address)
        } catch (e: MalformedURLException) {
            // What a terrible failure, go boom
            throw IllegalArgumentException("Invalid URL: $providerURL", e)
        } catch (e: IOException) {
            currentAddress.emit(AddressStatus.Error(e))
            Result.failure(e)
        }
    }
}
