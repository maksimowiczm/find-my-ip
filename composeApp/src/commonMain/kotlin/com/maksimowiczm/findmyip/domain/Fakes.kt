package com.maksimowiczm.findmyip.domain

import androidx.paging.PagingData
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.data.model.NetworkType
import java.util.Random
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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

/**
 * Fake implementation for screenshots preview
 */
@Suppress("unused")
object Fakes :
    ClearHistoryUseCase,
    ObserveAddressUseCase,
    ObserveHistoryUseCase,
    RefreshAddressesUseCase,
    ShouldShowHistoryUseCase,
    TestInternetProtocolsUseCase {

    private val random = Random()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val history = (0..20).flatMap {
        DEMO_IPS_V4 + DEMO_IPS_V6
    }.sortedBy { random.nextInt() }.map { ip ->
        ObserveHistoryUseCase.HistoryItem(
            id = random.nextLong(),
            ip = ip,
            date = Clock.System.now()
                .plus(random.nextInt(7).days)
                .plus(random.nextInt(24).hours)
                .plus(random.nextInt(60).minutes)
                .toLocalDateTime(TimeZone.Companion.currentSystemDefault()),
            protocol = if (ip in DEMO_IPS_V4) {
                InternetProtocolVersion.IPv4
            } else {
                InternetProtocolVersion.IPv6
            }
        )
    }.toMutableList()

    private fun randomNetworkType() = when (random.nextInt(3)) {
        0 -> NetworkType.WIFI
        1 -> NetworkType.MOBILE
        else -> NetworkType.VPN
    }

    private val addressV4 = MutableStateFlow<ObserveAddressUseCase.AddressStatus>(
        ObserveAddressUseCase.AddressStatus.Success(
            Address(
                ip = DEMO_IPS_V4.random(),
                networkType = randomNetworkType(),
                protocol = InternetProtocolVersion.IPv4
            )
        )
    )

    private val addressV6 = MutableStateFlow<ObserveAddressUseCase.AddressStatus>(
        ObserveAddressUseCase.AddressStatus.Success(
            Address(
                ip = DEMO_IPS_V6.random(),
                networkType = randomNetworkType(),
                protocol = InternetProtocolVersion.IPv6
            )
        )
    )

    override suspend fun clearHistory() {
        history.clear()
    }

    override fun observeAddress(protocol: InternetProtocolVersion) = when (protocol) {
        InternetProtocolVersion.IPv4 -> addressV4
        InternetProtocolVersion.IPv6 -> addressV6
    }

    override fun observeHistory(
        protocol: InternetProtocolVersion?
    ): Flow<PagingData<ObserveHistoryUseCase.HistoryItem>> = flowOf(
        PagingData.Companion.from(
            history.filter {
                protocol == null || it.protocol == protocol
            }
        )
    )

    override fun refreshAddresses() {
        coroutineScope.launch {
            addressV4.value = ObserveAddressUseCase.AddressStatus.Loading

            delay(random.nextLong(200, 1_000))

            addressV4.value = ObserveAddressUseCase.AddressStatus.Success(
                Address(
                    ip = DEMO_IPS_V4.random(),
                    networkType = randomNetworkType(),
                    protocol = InternetProtocolVersion.IPv4
                )
            )
        }

        coroutineScope.launch {
            addressV6.value = ObserveAddressUseCase.AddressStatus.Loading

            delay(random.nextLong(200, 1_000))

            addressV6.value = ObserveAddressUseCase.AddressStatus.Success(
                Address(
                    ip = DEMO_IPS_V6.random(),
                    networkType = randomNetworkType(),
                    protocol = InternetProtocolVersion.IPv6
                )
            )
        }
    }

    override fun shouldShowHistory(protocol: InternetProtocolVersion): Flow<Boolean> =
        flowOf(history.any { it.protocol == protocol })

    override suspend fun testInternetProtocols() {
        delay(1_000)
    }
}
