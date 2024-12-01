package com.maksimowiczm.findmyip.data.repository

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.data.model.NetworkType
import com.maksimowiczm.findmyip.data.network.CurrentAddressState
import java.util.Calendar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

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

class DemoPublicAddressRepository : PublicAddressRepository {
    private val v4AddressFlow = MutableStateFlow<CurrentAddressState>(CurrentAddressState.None)
    private val v6AddressFlow = MutableStateFlow<CurrentAddressState>(CurrentAddressState.None)

    override fun observeCurrentAddress(
        internetProtocolVersion: InternetProtocolVersion
    ): Flow<CurrentAddressState> {
        CoroutineScope(Dispatchers.Main).launch {
            when (internetProtocolVersion) {
                InternetProtocolVersion.IPv4 ->
                    if (v4AddressFlow.value is CurrentAddressState.None) {
                        refreshCurrentAddress(internetProtocolVersion)
                    }

                InternetProtocolVersion.IPv6 ->
                    if (v6AddressFlow.value is CurrentAddressState.None) {
                        refreshCurrentAddress(internetProtocolVersion)
                    }
            }
        }

        return when (internetProtocolVersion) {
            InternetProtocolVersion.IPv4 -> v4AddressFlow
            InternetProtocolVersion.IPv6 -> v6AddressFlow
        }
    }

    override suspend fun refreshCurrentAddress(
        internetProtocolVersion: InternetProtocolVersion
    ): Result<Address, Unit> {
        val currentAddressFlow = when (internetProtocolVersion) {
            InternetProtocolVersion.IPv4 -> v4AddressFlow
            InternetProtocolVersion.IPv6 -> v6AddressFlow
        }

        currentAddressFlow.emit(CurrentAddressState.Loading)
        delay(500)

        val ip = when (internetProtocolVersion) {
            InternetProtocolVersion.IPv4 -> DEMO_IPS_V4.random()
            InternetProtocolVersion.IPv6 -> DEMO_IPS_V6.random()
        }

        val address = Address(
            ip = ip,
            date = Calendar.getInstance().time,
            networkType = NetworkType.WIFI,
            internetProtocolVersion = internetProtocolVersion
        )

        currentAddressFlow.emit(CurrentAddressState.Success(address))
        return Ok(address)
    }

    private val random = java.util.Random()
    private val v4History = DEMO_IPS_V4.map { ip ->
        Address(
            ip = ip,
            date = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -random.nextInt(7))
                add(Calendar.HOUR_OF_DAY, -random.nextInt(24))
                add(Calendar.MINUTE, -random.nextInt(60))
            }.time,
            networkType = NetworkType.WIFI,
            internetProtocolVersion = InternetProtocolVersion.IPv4
        )
    }.sortedBy { it.date }.toMutableList()
    private val v6History = DEMO_IPS_V6.map { ip ->
        Address(
            ip = ip,
            date = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -random.nextInt(7))
                add(Calendar.HOUR_OF_DAY, -random.nextInt(24))
                add(Calendar.MINUTE, -random.nextInt(60))
            }.time,
            networkType = NetworkType.WIFI,
            internetProtocolVersion = InternetProtocolVersion.IPv6
        )
    }.sortedBy { it.date }.toMutableList()

    private val v4HistoryFlow = MutableStateFlow<List<Address>>(v4History)
    private val v6HistoryFlow = MutableStateFlow<List<Address>>(v6History)

    override suspend fun deleteAll() {
        v4History.clear()
        v6History.clear()
        v4HistoryFlow.emit(v4History)
        v6HistoryFlow.emit(v6History)
    }

    override fun observeAddressHistory(
        internetProtocolVersion: InternetProtocolVersion
    ): Flow<List<Address>> {
        return when (internetProtocolVersion) {
            InternetProtocolVersion.IPv4 -> v4HistoryFlow
            InternetProtocolVersion.IPv6 -> v6HistoryFlow
        }
    }

    override suspend fun insertIfDistinct(address: Address) {
        val addressHistory = when (address.internetProtocolVersion) {
            InternetProtocolVersion.IPv4 -> v4History
            InternetProtocolVersion.IPv6 -> v6History
        }

        if (addressHistory.isEmpty() || addressHistory.last().ip != address.ip) {
            addressHistory.add(address)
            addressHistory.sortBy { it.date }

            when (address.internetProtocolVersion) {
                InternetProtocolVersion.IPv4 -> v4HistoryFlow.emit(addressHistory.reversed())
                InternetProtocolVersion.IPv6 -> v6HistoryFlow.emit(addressHistory.reversed())
            }
        }
    }
}
