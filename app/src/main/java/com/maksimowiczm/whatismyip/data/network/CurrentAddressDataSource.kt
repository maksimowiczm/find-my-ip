package com.maksimowiczm.whatismyip.data.network

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

class CurrentAddressDataSource(
    private val networkDispatcher: CoroutineDispatcher
) {
    private val currentAddress = MutableStateFlow<Address?>(null)
    private val calender = Calendar.getInstance()

    fun observeCurrentAddress(autoFetch: Boolean): Flow<Address?> {
        if (autoFetch && currentAddress.value == null) {
            CoroutineScope(networkDispatcher).launch {
                refreshCurrentAddress()
            }
        }

        return currentAddress
    }

    suspend fun refreshCurrentAddress() {
        withContext(networkDispatcher) {
            currentAddress.emit(null)

            runCatching {
                URL("https://api.ipify.org").readText()
            }.map { address ->
                currentAddress.emit(
                    Address(
                        ip = address,
                        date = calender.time
                    )
                )
            }
        }
    }
}
