package com.maksimowiczm.findmyip.infrastructure.di

import com.maksimowiczm.findmyip.data.utils.DateProvider
import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.model.NetworkType
import com.maksimowiczm.findmyip.domain.source.AddressObserver
import com.maksimowiczm.findmyip.domain.source.AddressState
import com.maksimowiczm.findmyip.domain.source.NetworkAddress
import kotlin.random.Random
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class FakeAddressObserver(
    private val internetProtocol: InternetProtocol,
    private val random: Random = Random.Default,
    private val dateProvider: DateProvider = DateProvider {
        random.nextLong(1746500000000, 1747500000000)
    },
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) : AddressObserver {
    private val addresses: List<String> = when (internetProtocol) {
        InternetProtocol.IPv4 -> DEMO_IPS_V4
        InternetProtocol.IPv6 -> DEMO_IPS_V6
    }

    private val _flow = MutableStateFlow<AddressState?>(null)
    override val flow = _flow
        .onEach {
            if (it == null) {
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
            delay(random.nextLong(200, 1000))
            val response = addresses.random(random)
            val networkType = randomNetworkType(random)

            if (random.nextInt(0, 100) < 20) {
                error("Simulated error")
            }

            NetworkAddress(
                ip = response,
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

private val DEMO_IPS_V4 = listOf(
    // google.com
    "172.253.63.100",
    // github.com
    "140.82.121.3",
    // developer.android.com
    "142.250.203.142"
)

private val DEMO_IPS_V6 = listOf(
    // google.com
    "2001:4860:4860::8888",
    "2001:4860:4860:0:0:0:0:8888",
    "2001:4860:4860::8844",
    "2001:4860:4860:0:0:0:0:8844"
)

private fun randomNetworkType(random: Random) = when (random.nextInt(2)) {
    0 -> NetworkType.WiFi
    1 -> NetworkType.Cellular
    2 -> NetworkType.VPN
    else -> error("Invalid random value")
}
