package com.maksimowiczm.findmyip.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AddressDao {
    @Query(
        """
        SELECT * FROM AddressEntity a
        WHERE :protocol IS NULL OR a.internetProtocolVersion = :protocol
        ORDER BY a.timestamp DESC
        """
    )
    abstract fun observeAddresses(
        protocol: InternetProtocolVersion?
    ): PagingSource<Int, AddressEntity>

    @Query(
        """
        SELECT * FROM AddressEntity a
        WHERE a.internetProtocolVersion = :protocol
        ORDER BY a.timestamp DESC
        LIMIT 1
        """
    )
    abstract suspend fun getLatest(protocol: InternetProtocolVersion): AddressEntity?

    @Insert
    protected abstract suspend fun insert(address: AddressEntity)

    @Transaction
    open suspend fun insertIfDistinct(addressEntity: AddressEntity) {
        val latest = getLatest(addressEntity.internetProtocolVersion)
        if (latest == null || latest.ip != addressEntity.ip) {
            insert(addressEntity)
        }
    }

    @Transaction
    open suspend fun insertIfOlderThanLatest(addressEntity: AddressEntity, difference: Long) {
        val latest = getLatest(addressEntity.internetProtocolVersion)
        if (latest == null || addressEntity.timestamp - latest.timestamp > difference) {
            insert(addressEntity)
        }
    }

    @Query(
        """
        SELECT EXISTS  (  
            SELECT 1 FROM AddressEntity a
            WHERE a.internetProtocolVersion = :protocol
        )
        """
    )
    abstract fun isNotEmpty(protocol: InternetProtocolVersion): Flow<Boolean>

    @Query(
        """
        DELETE FROM AddressEntity
        """
    )
    abstract suspend fun deleteAll()
}
