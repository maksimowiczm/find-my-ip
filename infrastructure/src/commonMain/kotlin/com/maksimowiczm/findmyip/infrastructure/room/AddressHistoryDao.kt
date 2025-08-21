package com.maksimowiczm.findmyip.infrastructure.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maksimowiczm.findmyip.infrastructure.room.AddressVersionSQLConstants.IPV4
import com.maksimowiczm.findmyip.infrastructure.room.AddressVersionSQLConstants.IPV6

@Dao
internal interface AddressHistoryDao {

    @Query(
        """
        SELECT * 
        FROM AddressHistory
        WHERE
            (:query IS NULL OR address LIKE '%' || :query || '%') AND
            (
                (addressVersion = CASE WHEN :ipv4 THEN $IPV4 ELSE -1 END) OR
                (addressVersion = CASE WHEN :ipv6 THEN $IPV6 ELSE -1 END)
            )
        ORDER BY epochSeconds DESC
    """
    )
    fun observePaged(
        query: String?,
        ipv4: Boolean,
        ipv6: Boolean,
    ): PagingSource<Int, AddressHistoryEntity>

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
