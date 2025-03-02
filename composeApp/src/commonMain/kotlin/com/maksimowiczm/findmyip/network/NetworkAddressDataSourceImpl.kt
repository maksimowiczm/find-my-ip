package com.maksimowiczm.findmyip.network

import java.net.URL
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NetworkAddressDataSourceImpl(
    val providerURL: String,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : NetworkAddressDataSource {
    private val scope = CoroutineScope(ioDispatcher + SupervisorJob())
    private val currentAddress = MutableStateFlow<Result<String?>>(Result.success(null))

    init {
        refreshAddress()
    }

    override fun observeAddress(): Flow<Result<String?>> = currentAddress

    override fun refreshAddress() {
        scope.launch {
            currentAddress.emit(Result.success(null))

            val result = runCatching {
                URL(providerURL).readText()
            }

            currentAddress.emit(result)
        }
    }
}
