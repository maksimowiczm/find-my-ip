package com.maksimowiczm.findmyip.network

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

internal class NetworkAddressDataSourceImpl(
    private val providerURL: String,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : NetworkAddressDataSource,
    DisposableHandle {
    private val mutex = Mutex()
    private val ioScope = CoroutineScope(ioDispatcher)

    private val client = HttpClient {
        install(HttpTimeout) {
            connectTimeoutMillis = 5_000
            requestTimeoutMillis = 5_000
        }
    }

    private val state = MutableStateFlow<AddressStatus>(AddressStatus.InProgress)

    override val addressFlow = state.asStateFlow()

    override fun refreshAddress(): AddressRefreshResult = with(ioScope) {
        if (!mutex.tryLock()) {
            return AddressRefreshResult.AlreadyInProgress
        }

        launch {
            state.value = AddressStatus.InProgress

            val result = try {
                Logger.d { "Executing network request to $providerURL" }
                val response = client.get(providerURL)
                val ip = response.bodyAsText()
                AddressStatus.Success(NetworkAddress(ip))
            } catch (e: Exception) {
                AddressStatus.Error(e)
            }

            state.value = result
        }.invokeOnCompletion {
            mutex.unlock()
        }

        return AddressRefreshResult.Ok
    }

    override fun dispose() {
        client.close()
    }
}
