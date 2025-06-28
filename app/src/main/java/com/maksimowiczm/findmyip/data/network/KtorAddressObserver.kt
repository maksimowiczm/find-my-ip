package com.maksimowiczm.findmyip.data.network

import com.maksimowiczm.findmyip.data.utils.DateProvider
import com.maksimowiczm.findmyip.data.utils.defaultKotlinDateProvider
import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.source.AddressObserver
import com.maksimowiczm.findmyip.domain.source.AddressState
import com.maksimowiczm.findmyip.domain.source.NetworkAddress
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class KtorAddressObserver(
    private val url: String,
    private val client: HttpClient,
    private val internetProtocol: InternetProtocol,
    private val connectivityObserver: ConnectivityObserver,
    private val dateProvider: DateProvider = defaultKotlinDateProvider,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
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

    @OptIn(ExperimentalTime::class)
    override suspend fun refresh(): Result<NetworkAddress> {
        _flow.emit(AddressState.Refreshing)

        val result = runCatching {
            val response = client.get(url)

            if (response.status.value != 200) {
                error("Error: ${response.status.value}")
            }

            val responseText = response.bodyAsText()

            // Possible race conditions, how to handle this?
            val networkType = connectivityObserver.getNetworkType()
                ?: error("Network type is null")

            NetworkAddress(
                ip = responseText,
                networkType = networkType,
                internetProtocol = internetProtocol,
                dateTime = Instant
                    .fromEpochMilliseconds(dateProvider.currentTimeMillis())
                    .toLocalDateTime(TimeZone.currentSystemDefault())
            )
        }

        result.onSuccess {
            _flow.emit(AddressState.Success(it))
        }.onFailure {
            _flow.emit(AddressState.Error(it))
        }

        return result
    }
}
