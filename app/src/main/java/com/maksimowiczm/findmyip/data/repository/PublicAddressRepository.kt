package com.maksimowiczm.findmyip.data.repository

import androidx.room.withTransaction
import com.maksimowiczm.findmyip.data.database.AppDatabase
import com.maksimowiczm.findmyip.data.database.entity.AddressEntity
import com.maksimowiczm.findmyip.data.database.entity.AddressEntityDao
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.network.CurrentAddressDataSource
import java.util.Calendar
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PublicAddressRepository @Inject constructor(
    private val currentAddressDataSource: CurrentAddressDataSource,
    private val database: AppDatabase,
    private val addressEntityDao: AddressEntityDao
) {
    fun observeCurrentAddress() = currentAddressDataSource.observeCurrentAddress(autoFetch = true)

    /**
     * Fetches the current address and returns it.
     *
     * @return The current address or null if the fetch failed.
     */
    suspend fun refreshCurrentAddress() = currentAddressDataSource.refreshCurrentAddress()

    suspend fun deleteAll() = addressEntityDao.deleteAll()

    fun observeAddressHistory(): Flow<List<Address>> {
        return addressEntityDao.observeAddresses()
            .map { addressEntities ->
                addressEntities.map { it.toAddress() }
            }
    }

    /**
     * Inserts the given address into the database if it is different from the latest address.
     */
    suspend fun insertIfDistinct(address: Address) {
        database.withTransaction {
            val latestAddress = addressEntityDao.getLatestAddress()
            if (latestAddress == null || latestAddress.ip != address.ip) {
                val entity = address.toEntity()
                if (entity.timestamp != latestAddress?.timestamp) {
                    addressEntityDao.insertAddress(entity)
                }
            }
        }
    }
}

private fun AddressEntity.toAddress(): Address {
    return Address(
        ip = ip,
        date = Calendar.getInstance().apply { timeInMillis = timestamp.toLong() }.time
    )
}

private fun Address.toEntity(): AddressEntity {
    return AddressEntity(
        ip = ip,
        timestamp = date.time.toLong()
    )
}
