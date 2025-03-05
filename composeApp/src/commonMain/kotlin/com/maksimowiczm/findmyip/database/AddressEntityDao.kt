package com.maksimowiczm.findmyip.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion

@Dao
interface AddressEntityDao {
    @Query(
        """SELECT * 
           FROM addressentity 
           WHERE internetProtocolVersion == :internetProtocolVersion 
           ORDER BY timestamp DESC"""
    )
    fun observeAddressesPaged(
        internetProtocolVersion: InternetProtocolVersion
    ): PagingSource<Int, AddressEntity>

    @Query(
        """SELECT * 
           FROM addressentity 
           WHERE internetProtocolVersion == :internetProtocolVersion 
           ORDER BY timestamp DESC 
           LIMIT 1"""
    )
    suspend fun getLatestAddress(internetProtocolVersion: InternetProtocolVersion): AddressEntity?

    @Insert
    suspend fun insertAddress(addressEntity: AddressEntity)

    @Transaction
    suspend fun insertAddressIfOlderThanLatest(addressEntity: AddressEntity, difference: Long) {
        val latestAddress = getLatestAddress(addressEntity.internetProtocolVersion)
        if (latestAddress == null ||
            addressEntity.timestamp - latestAddress.timestamp > difference
        ) {
            insertAddress(addressEntity)
        }
    }

    @Query("DELETE FROM addressentity")
    suspend fun deleteAll()

    @Transaction
    suspend fun insertIfDistinct(addressEntity: AddressEntity) {
        val latestAddress = getLatestAddress(addressEntity.internetProtocolVersion)
        if (latestAddress?.ip != addressEntity.ip) {
            insertAddress(addressEntity)
        }
    }
}
