package com.maksimowiczm.findmyip.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.maksimowiczm.findmyip.data.model.AddressEntity
import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.source.AddressLocalDataSource
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AddressDao : AddressLocalDataSource {
    @Query(
        """
        SELECT *
        FROM Address
        WHERE 
          Ip LIKE '%' || :query || '%' AND
          (:start IS NULL OR EpochMillis >= :start) AND
          (:end IS NULL OR EpochMillis <= :end)
        ORDER BY EpochMillis DESC
        """
    )
    abstract override fun observeAddresses(
        query: String,
        start: Long?,
        end: Long?
    ): Flow<List<AddressEntity>>

    @Query("SELECT * FROM Address WHERE Id = :id")
    abstract override suspend fun getAddress(id: Long): AddressEntity?

    @Query(
        """
        SELECT * 
        FROM Address 
        WHERE 
          InternetProtocol = :protocol
        ORDER BY EpochMillis DESC 
        LIMIT 1
        """
    )
    protected abstract suspend fun getLatestAddress(protocol: InternetProtocol): AddressEntity?

    @Insert
    protected abstract suspend fun insertAddress(address: AddressEntity): Long?

    @Transaction
    override suspend fun insertAddressIfUniqueToLast(address: AddressEntity): Long? {
        val latestAddress = getLatestAddress(protocol = address.internetProtocol)
        return if (
            latestAddress == null ||
            latestAddress.ip != address.ip ||
            latestAddress.networkType != address.networkType
        ) {
            insertAddress(address)
        } else {
            null
        }
    }

    @Delete
    abstract override suspend fun deleteAddress(address: AddressEntity)
}
