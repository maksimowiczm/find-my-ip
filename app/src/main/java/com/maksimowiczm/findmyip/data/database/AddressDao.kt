package com.maksimowiczm.findmyip.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.maksimowiczm.findmyip.data.model.AddressEntity
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

    @Query("SELECT * FROM Address ORDER BY EpochMillis DESC LIMIT 1")
    protected abstract suspend fun getLastAddress(): AddressEntity?

    @Insert
    abstract override suspend fun insertAddress(address: AddressEntity)

    @Transaction
    override suspend fun insertAddressIfUniqueToLast(address: AddressEntity) {
        val lastAddress = getLastAddress()
        if (
            lastAddress == null ||
            lastAddress.ip != address.ip ||
            lastAddress.networkType != address.networkType ||
            lastAddress.internetProtocol != address.internetProtocol
        ) {
            insertAddress(address)
        }
    }

    @Delete
    abstract override suspend fun deleteAddress(address: AddressEntity)
}
