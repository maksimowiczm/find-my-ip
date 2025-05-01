package com.maksimowiczm.findmyip.data.network

import com.maksimowiczm.findmyip.domain.source.Address
import com.maksimowiczm.findmyip.domain.source.AddressObserver
import com.maksimowiczm.findmyip.domain.source.AddressState
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class KtorAddressObserver(
    private val url: String,
    private val client: HttpClient,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : AddressObserver {
    private val _flow = MutableStateFlow<AddressState?>(null)
    override val flow: Flow<AddressState> = _flow
        .onEach {
            if (it == null) {
                // Don't block the flow scope
                scope.launch {
                    refresh()
                }
            }
        }
        .filterNotNull()

    override suspend fun refresh(): Result<Address> {
        _flow.emit(AddressState.Refreshing)

        val address = runCatching {
            val response = client.get(url)

            if (response.status.value != 200) {
                error("Error: ${response.status.value}")
            }

            response.bodyAsText()
        }.map {
            Address(it)
        }

        address.onSuccess {
            _flow.emit(AddressState.Success(it))
        }.onFailure {
            _flow.emit(AddressState.Error(it))
        }

        return address
    }
}
