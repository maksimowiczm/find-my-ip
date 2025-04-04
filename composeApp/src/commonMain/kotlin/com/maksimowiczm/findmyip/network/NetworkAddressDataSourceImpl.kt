package com.maksimowiczm.findmyip.network

import co.touchlab.kermit.Logger
import com.maksimowiczm.findmyip.data.model.NetworkType
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class NetworkAddressDataSourceImpl(
    private val providerURL: String,
    private val connectivityObserver: ConnectivityObserver,
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

                // Run in parallel
                val (networkType, ip) = awaitAll(
                    async {
                        connectivityObserver.getNetworkType()
                    },
                    async {
                        runCatching {
                            val response = client.get(providerURL)
                            response.bodyAsText()
                        }
                    }
                )

                @Suppress("UNCHECKED_CAST")
                ip as Result<String>
                networkType as NetworkType

                AddressStatus.Success(
                    address = NetworkAddress(ip.getOrThrow()),
                    networkType = networkType
                )
            } catch (e: Exception) {
                AddressStatus.Error(e)
            }

            state.value = result
        }.invokeOnCompletion {
            mutex.unlock()
        }

        return AddressRefreshResult.Ok
    }

    override suspend fun blockingRefreshAddress(): Result<AddressStatus.Success> = mutex.withLock {
        runCatching {
            Logger.d { "Executing network request to $providerURL" }

            // Run in parallel
            val networkType = connectivityObserver.getNetworkType()
            val response = client.get(providerURL)
            val ip = response.bodyAsText()

            AddressStatus.Success(
                address = NetworkAddress(ip),
                networkType = networkType
            )
        }
    }

    override fun dispose() {
        client.close()
    }
}
