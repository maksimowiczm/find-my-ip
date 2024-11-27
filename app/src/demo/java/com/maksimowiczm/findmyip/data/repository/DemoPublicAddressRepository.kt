package com.maksimowiczm.findmyip.data.repository

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.network.CurrentAddressState
import java.util.Calendar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

private val DEMO_IPS = listOf(
    // google.com
    "172.253.63.100",
    // github.com
    "140.82.121.3",
    // developer.android.com
    "142.250.203.142"
)

class DemoPublicAddressRepository : PublicAddressRepository {
    private val currentAddressFlow = MutableStateFlow<CurrentAddressState>(CurrentAddressState.None)

    override fun observeCurrentAddress(): Flow<CurrentAddressState> {
        CoroutineScope(Dispatchers.Main).launch {
            refreshCurrentAddress()
        }

        return currentAddressFlow
    }

    override suspend fun refreshCurrentAddress(): Result<Address, Unit> {
        currentAddressFlow.emit(CurrentAddressState.Loading)
        delay(500)
        val ip = DEMO_IPS.random()
        val address = Address(
            ip = ip,
            date = Calendar.getInstance().time
        )
        currentAddressFlow.emit(CurrentAddressState.Success(address))
        return Ok(address)
    }

    private val random = java.util.Random()
    private val addressHistory = DEMO_IPS.map { ip ->
        Address(
            ip = ip,
            date = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -random.nextInt(7))
                add(Calendar.HOUR_OF_DAY, -random.nextInt(24))
                add(Calendar.MINUTE, -random.nextInt(60))
            }.time
        )
    }.sortedBy { it.date }.toMutableList()

    private val addressHistoryFlow = MutableStateFlow<List<Address>>(addressHistory)

    override suspend fun deleteAll() {
        addressHistory.clear()
        addressHistoryFlow.emit(addressHistory)
    }

    override fun observeAddressHistory(): Flow<List<Address>> = addressHistoryFlow

    override suspend fun insertIfDistinct(address: Address) {
        if (addressHistory.isEmpty() || addressHistory.last().ip != address.ip) {
            addressHistory.add(address)
            addressHistoryFlow.emit(addressHistory.reversed())
        }
    }
}
