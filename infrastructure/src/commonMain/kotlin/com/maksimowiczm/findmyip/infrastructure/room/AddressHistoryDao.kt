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
        ORDER BY epochSeconds DESC
    """
    )
    fun observePaged(): PagingSource<Int, AddressHistoryEntity>

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
