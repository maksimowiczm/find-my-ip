package com.maksimowiczm.findmyip.data

import androidx.paging.PagingData
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.data.model.NetworkType
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

// This exists only for screenshots and I am too lazy for now to implement this using source sets

@Suppress("unused")
fun Module.addDemoAddressRepository() {
    singleOf(::DemoAddressRepository).bind<AddressRepository>()
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

class DemoAddressRepository : AddressRepository {
    private val v4AddressFlow = MutableStateFlow<AddressStatus?>(null)
    private val v6AddressFlow = MutableStateFlow<AddressStatus?>(null)

    private val random = java.util.Random()
    private val v4History = (0..10).flatMap { DEMO_IPS_V4 }.map { ip ->
        Address(
            ip = ip,
            date = Clock.System.now()
                .plus(random.nextInt(7).days)
                .plus(random.nextInt(24).hours)
                .plus(random.nextInt(60).minutes)
                .toLocalDateTime(TimeZone.currentSystemDefault()),
            networkType = NetworkType.WIFI,
            protocolVersion = InternetProtocolVersion.IPv4
        )
    }.sortedBy { it.date }.toMutableList()
    private val v6History = (0..10).flatMap { DEMO_IPS_V6 }.map { ip ->
        Address(
            ip = ip,
            date = Clock.System.now()
                .plus(random.nextInt(7).days)
                .plus(random.nextInt(24).hours)
                .plus(random.nextInt(60).minutes)
                .toLocalDateTime(TimeZone.currentSystemDefault()),
            networkType = NetworkType.WIFI,
            protocolVersion = InternetProtocolVersion.IPv6
        )
    }.sortedBy { it.date }.toMutableList()

    private val v4HistoryFlow = MutableStateFlow<List<Address>>(v4History)
    private val v6HistoryFlow = MutableStateFlow<List<Address>>(v6History)

    override fun observeAddress(
        internetProtocolVersion: InternetProtocolVersion
    ): Flow<AddressStatus> {
        CoroutineScope(Dispatchers.Main).launch {
            when (internetProtocolVersion) {
                InternetProtocolVersion.IPv4 ->
                    if (v4AddressFlow.value == null) {
                        refreshAddress(internetProtocolVersion)
                    }

                InternetProtocolVersion.IPv6 ->
                    if (v6AddressFlow.value == null) {
                        refreshAddress(internetProtocolVersion)
                    }
            }
        }

        return when (internetProtocolVersion) {
            InternetProtocolVersion.IPv4 -> v4AddressFlow.filterNotNull()
            InternetProtocolVersion.IPv6 -> v6AddressFlow.filterNotNull()
        }
    }

    override suspend fun refreshAddresses() {
        coroutineScope {
            launch { refreshAddress(InternetProtocolVersion.IPv4) }
            launch { refreshAddress(InternetProtocolVersion.IPv6) }
        }
    }

    override suspend fun refreshAddress(
        internetProtocolVersion: InternetProtocolVersion
    ): AddressStatus {
        val currentAddressFlow = when (internetProtocolVersion) {
            InternetProtocolVersion.IPv4 -> v4AddressFlow
            InternetProtocolVersion.IPv6 -> v6AddressFlow
        }

        currentAddressFlow.emit(AddressStatus.Loading)
        delay(500)

        val ip = when (internetProtocolVersion) {
            InternetProtocolVersion.IPv4 -> DEMO_IPS_V4.random()
            InternetProtocolVersion.IPv6 -> DEMO_IPS_V6.random()
        }

        val address = Address(
            ip = ip,
            date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            networkType = NetworkType.WIFI,
            protocolVersion = internetProtocolVersion
        )

        currentAddressFlow.emit(AddressStatus.Success(address))
        return AddressStatus.Success(address)
    }

    override fun observeAddressesPaged(
        internetProtocolVersion: InternetProtocolVersion
    ): Flow<PagingData<Address>> = when (internetProtocolVersion) {
        InternetProtocolVersion.IPv4 -> v4HistoryFlow.map { PagingData.from(it) }
        InternetProtocolVersion.IPv6 -> v6HistoryFlow.map { PagingData.from(it) }
    }

    override suspend fun clearHistory() {
        v4History.clear()
        v6History.clear()
        v4HistoryFlow.emit(v4History)
        v6HistoryFlow.emit(v6History)
    }
}
