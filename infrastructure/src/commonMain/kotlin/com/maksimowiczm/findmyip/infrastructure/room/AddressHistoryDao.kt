package com.maksimowiczm.findmyip.infrastructure.room

import androidx.paging.PagingSource
import androidx.room.Dao
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
}
