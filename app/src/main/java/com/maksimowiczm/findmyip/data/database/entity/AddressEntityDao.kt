package com.maksimowiczm.findmyip.data.database.entity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressEntityDao {
    @Query(
        """SELECT * 
           FROM addressentity 
           WHERE internetProtocolVersion == :internetProtocolVersion 
           ORDER BY timestamp DESC"""
    )
    fun observeAddresses(
        internetProtocolVersion: InternetProtocolVersion
    ): Flow<List<AddressEntity>>

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

    @Query("DELETE FROM addressentity")
    suspend fun deleteAll()
}
