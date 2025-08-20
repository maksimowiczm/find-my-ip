package com.maksimowiczm.findmyip.infrastructure.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
internal interface AddressHistoryDao {

    @Query(
        """
        SELECT * 
        FROM AddressHistory
        WHERE
            (addressVersion = 4 AND :ipv4) OR
            (addressVersion = 6 AND :ipv6)
        ORDER BY epochSeconds DESC
    """
    )
    fun observePaged(ipv4: Boolean, ipv6: Boolean): PagingSource<Int, AddressHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insert(entity: AddressHistoryEntity)

    @Query(
        """
        SELECT * 
        FROM AddressHistory
        WHERE addressVersion = :version
        ORDER BY epochSeconds DESC
        LIMIT 1
    """
    )
    suspend fun getLatestAddress(version: AddressVersion): AddressHistoryEntity?
}
