package com.maksimowiczm.whatismyip.data.repository

import com.maksimowiczm.whatismyip.data.model.Address
import com.maksimowiczm.whatismyip.data.network.CurrentAddressDataSource
import java.util.Calendar
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PublicAddressRepository @Inject constructor(
    private val currentAddressDataSource: CurrentAddressDataSource
) {
    fun observeCurrentAddress() = currentAddressDataSource.observeCurrentAddress(autoFetch = true)

    suspend fun refreshCurrentAddress() = currentAddressDataSource.refreshCurrentAddress()

    fun observeAddressHistory(): Flow<List<Address>> = flow {
        delay(1000)
        emit(
            (0..10000).map {
                val time = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -it) }.time
                Address("127.0.0.1", time)
            }
        )
    }
}
